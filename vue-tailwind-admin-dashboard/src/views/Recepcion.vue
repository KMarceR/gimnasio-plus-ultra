<template>
  <AdminLayout>
    <div class="space-y-6">
      <header>
        <p class="text-sm font-medium text-brand-600">Control de acceso</p>
        <h1 class="mt-1 text-3xl font-semibold text-gray-900">Recepción</h1>
      </header>
      <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <section class="space-y-5 rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
          <h2 class="text-lg font-semibold text-gray-900">Marcar acceso</h2>
          <form class="flex gap-3" @submit.prevent="marcarPorCodigo">
            <input
              ref="campoCodigo"
              v-model="codigo"
              autofocus
              class="h-12 flex-1 rounded-xl border border-gray-200 px-4 font-mono text-sm outline-none focus:border-brand-500"
              placeholder="Escanee o pegue el código QR"
            />
            <Button type="submit" :disabled="procesando || !codigo.trim()">Registrar</Button>
          </form>

          <div>
            <p class="text-xs font-semibold uppercase tracking-wider text-gray-500">
              ¿Olvidó su carnet? Buscar por nombre
            </p>
            <input
              v-model="busqueda"
              class="mt-2 h-11 w-full rounded-xl border border-gray-200 px-4 text-sm outline-none focus:border-brand-500"
              placeholder="Nombre, apellido o DUI..."
            />
            <div v-if="sugerencias.length" class="mt-2 space-y-2">
              <button
                v-for="member in sugerencias"
                :key="member.id"
                type="button"
                class="flex w-full items-center gap-3 rounded-lg border border-gray-100 p-3 text-start hover:bg-gray-50"
                :disabled="procesando"
                @click="marcarCliente(member)"
              >
                <span
                  class="flex h-9 w-9 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
                  >{{ iniciales(member) }}</span
                >
                <span class="flex-1 text-sm font-medium text-gray-800">{{
                  nombreCompleto(member)
                }}</span>
                <span class="font-mono text-xs text-gray-400">{{ member.dui }}</span>
              </button>
            </div>
          </div>

          <Alert
            v-if="resultado"
            :variant="estiloResultado[resultado.tipoMovimiento].variante"
            :title="`${estiloResultado[resultado.tipoMovimiento].titulo} · ${resultado.nombreCliente}`"
            :message="detalleResultado"
          />
          <Alert v-if="error" variant="error" title="No se pudo registrar" :message="error" />
        </section>

        <div class="space-y-6">
          <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-500">Dentro ahora</p>
                <p class="mt-1 text-4xl font-semibold text-gray-900">{{ presentes.length }}</p>
              </div>
              <span class="rounded-full bg-brand-50 px-3 py-1 text-sm font-medium text-brand-600"
                >En el gimnasio</span
              >
            </div>
            <div class="mt-4 divide-y divide-gray-100">
              <div
                v-for="record in presentes"
                :key="record.id"
                class="flex items-center justify-between py-3"
              >
                <p class="text-sm font-semibold text-gray-800">{{ record.nombreCliente }}</p>
                <p class="text-xs text-gray-500">entró {{ record.hora }}</p>
              </div>
            </div>
          </section>

          <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
            <h2 class="text-lg font-semibold text-gray-900">Registro de hoy</h2>
            <div class="mt-4 divide-y divide-gray-100">
              <div
                v-for="record in registroHoy"
                :key="record.id"
                class="flex items-center justify-between py-3"
              >
                <div>
                  <p class="text-sm font-semibold text-gray-800">{{ record.nombreCliente }}</p>
                  <p class="text-xs text-gray-500">
                    {{ record.hora }}{{ record.horaSalida ? ` → ${record.horaSalida}` : '' }}
                  </p>
                </div>
                <Badge :color="colorEstado(record.estado)">{{
                  etiquetaAsistencia(record.estado)
                }}</Badge>
              </div>
            </div>
            <p v-if="!registroHoy.length" class="py-6 text-center text-sm text-gray-500">
              Sin registros hoy.
            </p>
          </section>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Alert from '@/components/ui/Alert.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'
import {
  useGymData,
  nombreCompleto,
  iniciales,
  etiquetaAsistencia,
  type AccesoQr,
  type AsistenciaRecord,
  type Member,
  type TipoMovimiento,
} from '@/composables/useGymData'

const { members, registrarAccesoQr, asistenciasDeHoy, presentesAhora } = useGymData()

const codigo = ref('')
const busqueda = ref('')
const procesando = ref(false)
const error = ref('')
const resultado = ref<AccesoQr | null>(null)
const presentes = ref<AsistenciaRecord[]>([])
const registroHoy = ref<AsistenciaRecord[]>([])
const campoCodigo = ref<HTMLInputElement | null>(null)

const estiloResultado: Record<
  TipoMovimiento,
  { titulo: string; variante: 'success' | 'info' | 'warning' | 'error' }
> = {
  ENTRADA: { titulo: 'Entrada', variante: 'success' },
  SALIDA: { titulo: 'Salida', variante: 'info' },
  DUPLICADO: { titulo: 'Ya registrado', variante: 'warning' },
  RECHAZADO: { titulo: 'Acceso denegado', variante: 'error' },
}

const detalleResultado = computed(() => {
  const r = resultado.value
  if (!r) return ''
  const partes = [r.mensaje]
  if (r.tipoMovimiento === 'SALIDA' && r.horaEntrada && r.horaSalida) {
    partes.push(`${r.horaEntrada.slice(0, 5)} → ${r.horaSalida.slice(0, 5)}`)
  }
  if (r.nombrePlan) {
    partes.push(`${r.nombrePlan} · vence ${r.fechaVencimiento}`)
  }
  return partes.join(' · ')
})

const sugerencias = computed(() => {
  const texto = busqueda.value.trim().toLowerCase()
  if (!texto) return []
  return members.value
    .filter((member) => `${nombreCompleto(member)} ${member.dui}`.toLowerCase().includes(texto))
    .slice(0, 5)
})

const coloresEstado: Record<string, 'success' | 'primary' | 'error'> = {
  Activo: 'success',
  Completada: 'primary',
  Cancelada: 'error',
}
const colorEstado = (estado: string) => coloresEstado[estado] ?? 'warning'

const actualizarListas = async () => {
  const [hoy, dentro] = await Promise.all([asistenciasDeHoy(), presentesAhora()])
  registroHoy.value = hoy
  presentes.value = dentro
}

// El codigo QR y la busqueda por nombre usan el mismo endpoint y las mismas reglas
const marcar = async (codigoQr: string) => {
  procesando.value = true
  error.value = ''
  resultado.value = null
  try {
    resultado.value = await registrarAccesoQr(codigoQr.trim())
    codigo.value = ''
    busqueda.value = ''
    await actualizarListas()
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'No se pudo registrar el acceso.'
  } finally {
    procesando.value = false
    campoCodigo.value?.focus()
  }
}
const marcarPorCodigo = () => marcar(codigo.value)
const marcarCliente = (member: Member) => marcar(member.codigoQr)

onMounted(() => {
  actualizarListas().catch((e) => {
    error.value = e instanceof Error ? e.message : 'No se pudieron cargar los registros.'
  })
})
</script>
