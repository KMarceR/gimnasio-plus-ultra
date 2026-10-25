<template>
  <AdminLayout>
    <div v-if="member" class="space-y-6">
      <button
        class="flex items-center gap-2 text-sm font-medium text-gray-600"
        @click="router.push('/clientes')"
      >
        <span class="flex h-9 w-9 items-center justify-center rounded-full border border-gray-200"
          >←</span
        >
        Perfil del Cliente
      </button>
      <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs sm:p-8">
        <div class="flex flex-col gap-5 sm:flex-row sm:items-start">
          <span
            class="flex h-20 w-20 items-center justify-center rounded-full bg-blue-light-100 text-2xl font-semibold text-blue-light-600"
            >{{ iniciales(member) }}</span
          >
          <div class="flex-1">
            <h1 class="text-3xl font-semibold text-gray-900">{{ nombreCompleto(member) }}</h1>
            <p class="mt-1 text-sm text-gray-500">{{ member.email }}</p>
            <Badge class="mt-3" :color="statusColor(member.estado)">{{ member.estado }}</Badge>
          </div>
          <button
            v-if="imagenQr"
            type="button"
            class="flex flex-col items-center rounded-xl border border-gray-200 p-3 transition hover:border-brand-400"
            title="Clic para copiar el código"
            @click="copiarCodigo"
          >
            <img :src="imagenQr" alt="Código QR del cliente" class="h-32 w-32" />
            <span class="mt-2 max-w-40 break-all font-mono text-[10px] text-gray-500">{{
              member.codigoQr
            }}</span>
            <span class="mt-1 text-xs font-medium text-brand-600">{{
              copiado ? '¡Código copiado!' : 'Clic para copiar'
            }}</span>
          </button>
        </div>
        <div class="mt-6 grid grid-cols-2 gap-3 lg:grid-cols-4">
          <div class="rounded-xl border border-gray-200 bg-gray-50 p-4">
            <p class="text-xs text-gray-500">Asistencias</p>
            <p class="mt-1 font-mono text-lg font-semibold text-gray-900">
              {{ member ? ingresosDeCliente(member.id) : 0 }}
            </p>
          </div>
          <div class="rounded-xl border border-gray-200 bg-gray-50 p-4">
            <p class="text-xs text-gray-500">Plan</p>
            <p class="mt-1 text-sm font-semibold text-gray-900">
              {{ currentPlan?.nombre || 'Sin plan' }}
            </p>
          </div>
          <div class="rounded-xl border border-gray-200 bg-gray-50 p-4">
            <p class="text-xs text-gray-500">DUI</p>
            <p class="mt-1 font-mono text-sm font-semibold text-gray-900">{{ member.dui }}</p>
          </div>
          <div class="rounded-xl border border-gray-200 bg-gray-50 p-4">
            <p class="text-xs text-gray-500">Miembro desde</p>
            <p class="mt-1 font-mono text-sm font-semibold text-gray-900">{{ member.creadoEn }}</p>
          </div>
        </div>
      </section>
      <nav
        class="flex w-fit max-w-full overflow-x-auto rounded-2xl border border-gray-200 bg-white p-1"
      >
        <button
          v-for="tabName in tabs"
          :key="tabName"
          :class="tab === tabName ? 'bg-white text-gray-900 shadow-theme-sm' : 'text-gray-500'"
          class="whitespace-nowrap rounded-xl px-5 py-2.5 text-sm font-medium"
          @click="tab = tabName"
        >
          {{ tabName }}
        </button>
      </nav>
      <section
        v-if="tab === 'Perfil'"
        class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs"
      >
        <div class="flex items-center justify-between">
          <h2 class="text-lg font-semibold text-gray-900">Información Personal</h2>
          <Button v-if="!editing" size="sm" variant="outline" @click="startEditing">Editar</Button>
        </div>
        <div v-if="editing" class="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2">
          <label
            v-for="field in editableFields"
            :key="field.key"
            class="text-xs font-semibold uppercase tracking-wider text-gray-500"
            :class="field.key === 'direccion' ? 'sm:col-span-2' : ''"
            >{{ field.label
            }}<input
              v-model="editForm[field.key]"
              :type="field.type"
              :pattern="field.pattern || undefined"
              :maxlength="field.maxlength || undefined"
              :title="field.title || undefined"
              class="mt-2 h-11 w-full rounded-lg border border-gray-200 bg-gray-50 px-3 text-sm" /></label
          ><textarea
            v-model="editForm.notas"
            class="sm:col-span-2 rounded-lg border border-gray-200 bg-gray-50 p-3 text-sm"
            placeholder="Notas internas"
          ></textarea>
          <Alert
            v-if="errorEdicion"
            class="sm:col-span-2"
            variant="error"
            title="No se pudieron guardar los cambios"
            :message="errorEdicion"
          />
          <div class="flex gap-3 sm:col-span-2">
            <Button size="sm" variant="outline" @click="editing = false">Cancelar</Button
            ><Button size="sm" :disabled="procesando" @click="saveEditing">Guardar cambios</Button>
          </div>
        </div>
        <div v-else class="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2">
          <InfoField label="Nombres" :value="member.nombres" /><InfoField
            label="Apellidos"
            :value="member.apellidos"
          /><InfoField label="Género" :value="member.genero" /><InfoField
            label="DUI"
            :value="member.dui"
          /><InfoField label="Email" :value="member.email" /><InfoField
            label="Teléfono"
            :value="member.telefono"
          /><InfoField label="Fecha de nacimiento" :value="member.fechaNacimiento" /><InfoField
            label="Dirección"
            :value="member.direccion || 'No registrada'"
            wide
          /><InfoField label="Notas" :value="member.notas || 'Sin notas registradas'" wide />
        </div>
      </section>
      <section v-else-if="tab === 'Suscripción'" class="space-y-5">
        <div class="rounded-2xl border border-brand-200 bg-brand-50 p-6">
          <div class="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
            <div>
              <p class="text-xs font-semibold uppercase text-brand-600">Plan actual</p>
              <h2 class="mt-2 text-2xl font-semibold text-gray-900">
                {{ currentPlan?.nombre || 'Sin plan asignado' }}
              </h2>
              <p class="mt-3 max-w-xl text-sm text-gray-600">{{ currentPlan?.detalles }}</p>
            </div>
            <p class="text-3xl font-bold text-brand-600">
              ${{ currentPlan?.precio.toLocaleString('es-SV') }}
            </p>
          </div>
        </div>
        <div class="rounded-2xl border border-gray-200 bg-white p-6">
          <h2 class="font-semibold text-gray-900">Vender o renovar suscripción</h2>
          <p class="mt-1 text-sm text-gray-500">
            Si el cliente tiene una suscripción vigente, la nueva empieza el día siguiente a su
            vencimiento.
          </p>
          <div class="mt-4 flex flex-col gap-3 sm:flex-row">
            <select
              v-model="planVenta"
              class="h-11 w-full rounded-lg border border-gray-200 px-3 text-sm sm:max-w-md"
            >
              <option v-for="plan in planesActivos" :key="plan.id" :value="plan.id">
                {{ plan.nombre }} · {{ plan.tipoPlan }} · ${{ plan.precio.toLocaleString('es-SV') }}
              </option>
            </select>
            <Button size="sm" :disabled="procesando || !planVenta" @click="vender">Vender</Button>
          </div>
          <Alert
            v-if="mensajeVenta"
            class="mt-4"
            variant="success"
            title="Listo"
            :message="mensajeVenta"
          />
          <Alert
            v-if="errorVenta"
            class="mt-4"
            variant="error"
            title="No se pudo completar"
            :message="errorVenta"
          />
        </div>
        <div class="overflow-x-auto rounded-2xl border border-gray-200 bg-white p-6">
          <h2 class="font-semibold text-gray-900">Historial de suscripciones</h2>
          <table class="mt-4 min-w-full">
            <thead>
              <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
                <th class="py-3 text-start">Plan</th>
                <th class="py-3 text-start">Inicio</th>
                <th class="py-3 text-start">Vence</th>
                <th class="py-3 text-start">Pagado</th>
                <th class="py-3 text-start">Estado</th>
                <th class="py-3 text-end"></th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="suscripcion in suscripciones"
                :key="suscripcion.idSuscripcion"
                class="border-b border-gray-100 last:border-0"
              >
                <td class="py-3 text-sm font-semibold text-gray-800">
                  {{ suscripcion.nombrePlan }}
                  <span class="block text-xs font-normal text-gray-500">{{
                    suscripcion.tipoPlan
                  }}</span>
                </td>
                <td class="py-3 font-mono text-sm text-gray-600">{{ suscripcion.fechaInicio }}</td>
                <td class="py-3 font-mono text-sm text-gray-600">
                  {{ suscripcion.fechaVencimiento }}
                </td>
                <td class="py-3 text-sm text-gray-800">
                  ${{ suscripcion.precioPagado.toLocaleString('es-SV') }}
                </td>
                <td class="py-3">
                  <Badge :color="suscripcion.vigente ? 'success' : estadoSuscripcionColor(suscripcion)">{{
                    suscripcion.vigente ? 'Vigente' : suscripcion.nombreEstado
                  }}</Badge>
                </td>
                <td class="py-3 text-end">
                  <button
                    v-if="suscripcion.nombreEstado === 'Activo'"
                    class="rounded-lg px-3 py-2 text-sm font-medium text-error-600 hover:bg-error-50"
                    :disabled="procesando"
                    @click="cancelar(suscripcion)"
                  >
                    Cancelar
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <p v-if="!suscripciones.length" class="py-6 text-center text-sm text-gray-500">
            Este cliente no tiene suscripciones registradas.
          </p>
        </div>
      </section>
      <section
        v-else-if="tab === 'Asistencia'"
        class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs"
      >
        <h2 class="text-lg font-semibold text-gray-900">Registro de asistencia</h2>
        <div class="mt-5 divide-y divide-gray-100">
          <div
            v-for="record in misAsistencias"
            :key="record.id"
            class="flex items-center justify-between py-3"
          >
            <div>
              <p class="text-sm font-semibold text-gray-800">{{ record.fecha }}</p>
              <p class="text-xs text-gray-500">
                {{ record.hora }}{{ record.horaSalida ? ` → ${record.horaSalida}` : '' }}
              </p>
            </div>
            <Badge :color="esIngreso(record) ? 'primary' : 'error'">{{
              etiquetaAsistencia(record.estado)
            }}</Badge>
          </div>
        </div>
        <p v-if="!misAsistencias.length" class="py-6 text-center text-sm text-gray-500">
          Sin registros de asistencia.
        </p>
      </section>
      <section
        v-else-if="tab === 'Sesiones'"
        class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs"
      >
        <h2 class="text-lg font-semibold text-gray-900">Sesiones con entrenador</h2>
        <div class="mt-5 divide-y divide-gray-100">
          <div
            v-for="session in misSesiones"
            :key="session.id"
            class="flex items-center justify-between py-3"
          >
            <div>
              <p class="text-sm font-semibold text-gray-800">
                {{
                  trainerById(session.entrenadorId)
                    ? nombreCompleto(trainerById(session.entrenadorId)!)
                    : 'Entrenador eliminado'
                }}
              </p>
              <p class="text-xs text-gray-500">
                {{ session.fecha }} · {{ session.horaInicio }} - {{ session.horaFin }}
              </p>
            </div>
            <Badge :color="sessionColor(session.estado)">{{ session.estado }}</Badge>
          </div>
        </div>
        <p v-if="!misSesiones.length" class="py-6 text-center text-sm text-gray-500">
          Este cliente no tiene sesiones registradas.
        </p>
      </section>
      <section v-else class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
        <h2 class="text-lg font-semibold text-gray-900">Notas internas</h2>
        <textarea
          v-model="notas"
          rows="8"
          class="mt-5 w-full rounded-xl border border-gray-200 bg-gray-50 p-4 text-sm outline-none focus:border-brand-500"
        ></textarea
        ><Button class-name="mt-5" @click="saveNotas">✓ Guardar notas</Button>
      </section>
    </div>
    <div v-else class="rounded-2xl bg-white p-8 text-center text-gray-500">
      {{ members.length ? 'Cliente no encontrado.' : 'Cargando...' }}
    </div>
    <Modal v-if="porCancelar" full-screen-backdrop @close="porCancelar = null">
      <template #body>
        <div class="p-4">
          <div class="w-full max-w-xl rounded-2xl bg-white shadow-theme-xl">
            <header class="border-b border-gray-200 p-6">
              <h2 class="text-xl font-semibold text-gray-900">
                Cancelar {{ porCancelar.suscripcion.nombrePlan }}
              </h2>
            </header>
            <div class="space-y-4 p-6 text-sm text-gray-600">
              <template v-if="porCancelar.vista.cambios.length">
                <p>Al cancelar, estos planes se adelantarán:</p>
                <div
                  v-for="cambio in porCancelar.vista.cambios"
                  :key="cambio.idSuscripcion"
                  class="rounded-xl border border-gray-200 p-4"
                >
                  <p class="font-semibold text-gray-900">{{ cambio.nombrePlan }}</p>
                  <p class="mt-1 text-gray-500 line-through">
                    {{ cambio.fechaInicioActual }} → {{ cambio.fechaVencimientoActual }}
                  </p>
                  <p class="font-medium text-brand-600">
                    {{ cambio.nuevaFechaInicio }} → {{ cambio.nuevaFechaVencimiento }}
                  </p>
                </div>
              </template>
              <Alert
                v-else-if="porCancelar.vista.quedaSinPlanVigente"
                variant="warning"
                title="El cliente quedará sin plan vigente"
                message="Su código QR será rechazado en la entrada hasta que compre un nuevo plan."
              />
              <p v-else>Ningún otro plan cambia de fecha.</p>
            </div>
            <footer class="flex gap-3 border-t border-gray-100 p-6">
              <Button
                variant="outline"
                class-name="flex-1 justify-center"
                @click="porCancelar = null"
                >Volver</Button
              >
              <Button
                class-name="flex-1 justify-center"
                :disabled="procesando"
                @click="confirmarCancelacion"
                >Confirmar cancelación</Button
              >
            </footer>
          </div>
        </div>
      </template>
    </Modal>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import QRCode from 'qrcode'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import {
  useGymData,
  nombreCompleto,
  iniciales,
  esIngreso,
  etiquetaAsistencia,
  type EstadoGeneral,
  type SessionStatus,
  type Suscripcion,
  type VistaCancelacion,
} from '@/composables/useGymData'
import InfoField from '@/components/common/InfoField.vue'
import Alert from '@/components/ui/Alert.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'
import Modal from '@/components/ui/Modal.vue'

const route = useRoute()
const router = useRouter()
const {
  members,
  memberById,
  updateMember,
  plans,
  trainerById,
  sessions,
  attendanceByMember,
  ingresosDeCliente,
  suscripcionesDeCliente,
  venderSuscripcion,
  vistaCancelacion,
  cancelarSuscripcion,
} = useGymData()
const member = computed(() => memberById(String(route.params.id)))
const tab = ref('Perfil')
const editing = ref(false)
const procesando = ref(false)
const errorEdicion = ref('')
const notas = ref('')
const tabs = ['Perfil', 'Suscripción', 'Asistencia', 'Sesiones' /*'Notas'*/]
const editForm = reactive({
  nombres: '',
  apellidos: '',
  email: '',
  telefono: '',
  dui: '',
  fechaNacimiento: '',
  direccion: '',
  notas: '',
})
const editableFields = [
  { key: 'nombres', label: 'Nombres', type: 'text' },
  { key: 'apellidos', label: 'Apellidos', type: 'text' },
  { key: 'email', label: 'Email', type: 'email' },
  {
    key: 'telefono',
    label: 'Teléfono',
    type: 'text',
    pattern: '\\d{4}-\\d{4}',
    maxlength: 9,
    title: 'Formato 0000-0000',
  },
  { key: 'dui', label: 'DUI', type: 'text', pattern: '\\d{8}-\\d', maxlength: 10, title: 'Formato 00000000-0' },
  { key: 'fechaNacimiento', label: 'Fecha de nacimiento', type: 'date' },
  { key: 'direccion', label: 'Dirección', type: 'text' },
] as {
  key: keyof typeof editForm
  label: string
  type: string
  pattern?: string
  maxlength?: number
  title?: string
}[]
const planesActivos = computed(() => plans.value.filter((plan) => plan.estado === 'Activo'))
const currentPlan = computed(() => plans.value.find((plan) => plan.id === member.value?.planId))
const misAsistencias = computed(() => (member.value ? attendanceByMember(member.value.id) : []))
const misSesiones = computed(() =>
  sessions.value.filter((session) => session.clienteId === member.value?.id),
)
const statusColor = (estado: EstadoGeneral) =>
  ({ Activo: 'primary', Inactivo: 'light' })[estado] as 'primary' | 'light'
const sessionColor = (estado: SessionStatus) =>
  ({ Programada: 'info', Confirmada: 'primary', Completada: 'success', Cancelada: 'light' })[
    estado
  ] as 'info' | 'primary' | 'success' | 'light'
const estadoSuscripcionColor = (suscripcion: Suscripcion) =>
  suscripcion.nombreEstado === 'Activo'
    ? 'info'
    : suscripcion.nombreEstado === 'Vencida'
      ? 'warning'
      : 'light'
const mensajeError = (e: unknown) => (e instanceof Error ? e.message : 'Ocurrió un error inesperado.')

const imagenQr = ref('')
const copiado = ref(false)
watch(
  () => member.value?.codigoQr,
  async (codigo) => {
    imagenQr.value = codigo ? await QRCode.toDataURL(codigo, { width: 256, margin: 1 }) : ''
  },
  { immediate: true },
)
const copiarCodigo = async () => {
  if (!member.value) return
  await navigator.clipboard.writeText(member.value.codigoQr)
  copiado.value = true
  setTimeout(() => (copiado.value = false), 2000)
}

const suscripciones = ref<Suscripcion[]>([])
const planVenta = ref('')
const mensajeVenta = ref('')
const errorVenta = ref('')
const cargarSuscripciones = async () => {
  if (!member.value) return
  try {
    suscripciones.value = await suscripcionesDeCliente(member.value.id)
  } catch (e) {
    errorVenta.value = mensajeError(e)
  }
}
watch(
  () => member.value?.id,
  () => {
    notas.value = member.value?.notas ?? ''
    planVenta.value = member.value?.planId ?? planesActivos.value[0]?.id ?? ''
    cargarSuscripciones()
  },
  { immediate: true },
)
const vender = async () => {
  if (!member.value) return
  procesando.value = true
  mensajeVenta.value = ''
  errorVenta.value = ''
  try {
    await venderSuscripcion(member.value.id, planVenta.value)
    await cargarSuscripciones()
    mensajeVenta.value = 'Suscripción registrada.'
  } catch (e) {
    errorVenta.value = mensajeError(e)
  } finally {
    procesando.value = false
  }
}
// Primero se muestra el aviso con lo que calculo el backend; se cancela al confirmar
const porCancelar = ref<{ suscripcion: Suscripcion; vista: VistaCancelacion } | null>(null)
const cancelar = async (suscripcion: Suscripcion) => {
  mensajeVenta.value = ''
  errorVenta.value = ''
  try {
    porCancelar.value = { suscripcion, vista: await vistaCancelacion(suscripcion.idSuscripcion) }
  } catch (e) {
    errorVenta.value = mensajeError(e)
  }
}
const confirmarCancelacion = async () => {
  if (!porCancelar.value) return
  procesando.value = true
  try {
    await cancelarSuscripcion(porCancelar.value.suscripcion.idSuscripcion)
    await cargarSuscripciones()
    mensajeVenta.value = 'Suscripción cancelada.'
  } catch (e) {
    errorVenta.value = mensajeError(e)
  } finally {
    procesando.value = false
    porCancelar.value = null
  }
}

const startEditing = () => {
  if (!member.value) return
  const { nombres, apellidos, email, telefono, dui, fechaNacimiento, direccion } = member.value
  Object.assign(editForm, {
    nombres,
    apellidos,
    email,
    telefono,
    dui,
    fechaNacimiento,
    direccion,
    notas: member.value.notas,
  })
  errorEdicion.value = ''
  editing.value = true
}
const saveEditing = async () => {
  if (!member.value) return
  procesando.value = true
  errorEdicion.value = ''
  try {
    await updateMember(member.value.id, { ...editForm })
    editing.value = false
  } catch (e) {
    errorEdicion.value = mensajeError(e)
  } finally {
    procesando.value = false
  }
}
const saveNotas = async () => {
  if (member.value) await updateMember(member.value.id, { notas: notas.value })
}
</script>
