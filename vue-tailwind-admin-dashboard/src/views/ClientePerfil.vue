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
        </div>
        <div class="mt-6 grid grid-cols-2 gap-3 lg:grid-cols-4">
          <div class="rounded-xl border border-gray-200 bg-gray-50 p-4">
            <p class="text-xs text-gray-500">Asistencias</p>
            <p class="mt-1 font-mono text-lg font-semibold text-gray-900">
              {{ misAsistencias.length }}
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
              class="mt-2 h-11 w-full rounded-lg border border-gray-200 bg-gray-50 px-3 text-sm" /></label
          ><textarea
            v-model="editForm.notas"
            class="sm:col-span-2 rounded-lg border border-gray-200 bg-gray-50 p-3 text-sm"
            placeholder="Notas internas"
          ></textarea>
          <div class="flex gap-3 sm:col-span-2">
            <Button size="sm" variant="outline" @click="editing = false">Cancelar</Button
            ><Button size="sm" @click="saveEditing">Guardar cambios</Button>
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
          <h2 class="font-semibold text-gray-900">Cambiar plan</h2>
          <select
            class="mt-4 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm sm:max-w-md"
            :value="member.planId"
            @change="changePlan"
          >
            <option v-for="plan in plans" :key="plan.id" :value="plan.id">
              {{ plan.nombre }} · ${{ plan.precio.toLocaleString('es-SV') }}
            </option>
          </select>
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
              <p class="text-xs text-gray-500">{{ record.hora }}</p>
            </div>
            <Badge
              :color="
                record.estado === 'Activo' || record.estado === 'Completada' ? 'primary' : 'light'
              "
              >{{ record.estado }}</Badge
            >
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
      Cliente no encontrado.
    </div>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import {
  useGymData,
  nombreCompleto,
  iniciales,
  type EstadoGeneral,
  type SessionStatus,
} from '@/composables/useGymData'
import InfoField from '@/components/common/InfoField.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'

const route = useRoute()
const router = useRouter()
const {
  memberById,
  updateMember,
  plans,
  changeMemberPlan,
  trainerById,
  sessions,
  attendanceByMember,
} = useGymData()
const member = memberById(String(route.params.id))
const tab = ref('Perfil')
const editing = ref(false)
const notas = ref(member?.notas || '')
const tabs = ['Perfil', 'Suscripción', 'Asistencia', 'Sesiones' /*'Notas'*/]
const editForm = reactive({
  nombres: member?.nombres || '',
  apellidos: member?.apellidos || '',
  email: member?.email || '',
  telefono: member?.telefono || '',
  dui: member?.dui || '',
  fechaNacimiento: member?.fechaNacimiento || '',
  direccion: member?.direccion || '',
  notas: member?.notas || '',
})
const editableFields = [
  { key: 'nombres', label: 'Nombres', type: 'text' },
  { key: 'apellidos', label: 'Apellidos', type: 'text' },
  { key: 'email', label: 'Email', type: 'email' },
  { key: 'telefono', label: 'Teléfono', type: 'text' },
  { key: 'dui', label: 'DUI', type: 'text' },
  { key: 'fechaNacimiento', label: 'Fecha de nacimiento', type: 'date' },
  { key: 'direccion', label: 'Dirección', type: 'text' },
] as const
const currentPlan = computed(() => plans.value.find((plan) => plan.id === member?.planId))
const misAsistencias = computed(() => (member ? attendanceByMember(member.id) : []))
const misSesiones = computed(() =>
  sessions.value.filter((session) => session.clienteId === member?.id),
)
const statusColor = (estado: EstadoGeneral) =>
  ({ Activo: 'primary', Inactivo: 'light' })[estado] as 'primary' | 'light'
const sessionColor = (estado: SessionStatus) =>
  ({ Programada: 'info', Confirmada: 'primary', Completada: 'success', Cancelada: 'light' })[
    estado
  ] as 'info' | 'primary' | 'success' | 'light'
const startEditing = () => {
  editing.value = true
}
const saveEditing = () => {
  if (member) updateMember(member.id, { ...editForm })
  editing.value = false
}
const saveNotas = () => {
  if (member) updateMember(member.id, { notas: notas.value })
}
const changePlan = (event: Event) => {
  const planId = (event.target as HTMLSelectElement).value
  if (member) changeMemberPlan(member.id, planId)
}
</script>
