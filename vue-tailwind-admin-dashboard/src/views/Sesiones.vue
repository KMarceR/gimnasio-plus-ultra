<template>
  <AdminLayout>
    <div class="space-y-6">
      <header class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Agenda operativa</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Control de Sesiones</h1>
          <p class="mt-1 text-sm text-gray-500">{{ sessions.length }} sesiones registradas</p>
        </div>
        <Button size="sm" @click="openCreateModal">+ Nueva Sesión</Button>
      </header>

      <section class="flex flex-wrap items-center gap-x-6 gap-y-2 text-sm text-gray-500">
        <span
          v-for="status in statusSummary"
          :key="status.label"
          class="inline-flex items-center gap-2"
        >
          <span :class="status.dot" class="h-2.5 w-2.5 rounded-full"></span>
          <strong :class="status.text">{{ status.count }}</strong> {{ status.label }}
        </span>
      </section>

      <section class="rounded-2xl border border-gray-200 bg-white p-4 shadow-theme-xs sm:p-5">
        <div class="flex flex-col gap-3 xl:flex-row xl:items-center">
          <div class="flex rounded-lg bg-gray-100 p-1">
            <button
              :class="
                viewMode === 'agenda' ? 'bg-white text-gray-900 shadow-theme-xs' : 'text-gray-500'
              "
              class="rounded-md px-4 py-2 text-sm font-medium"
              @click="viewMode = 'agenda'"
            >
              Agenda
            </button>
            <button
              :class="
                viewMode === 'week' ? 'bg-white text-gray-900 shadow-theme-xs' : 'text-gray-500'
              "
              class="rounded-md px-4 py-2 text-sm font-medium"
              @click="viewMode = 'week'"
            >
              Semana
            </button>
          </div>
          <input
            v-model="search"
            class="h-10 min-w-0 flex-1 rounded-lg border border-gray-200 px-3 text-sm outline-none focus:border-brand-500"
            placeholder="Buscar cliente o entrenador..."
          />
          <select
            v-model="trainerFilter"
            class="h-10 rounded-lg border border-gray-200 px-3 text-sm text-gray-700 outline-none focus:border-brand-500"
          >
            <option value="Todos">Todos los entrenadores</option>
            <option v-for="trainer in activeTrainers" :key="trainer.id" :value="trainer.id">
              {{ nombreCompleto(trainer) }}
            </option>
          </select>
        </div>
        <div class="mt-3 flex flex-wrap gap-2">
          <button
            v-for="status in statuses"
            :key="status"
            :class="
              statusFilter === status
                ? 'bg-brand-500 text-white'
                : 'bg-gray-50 text-gray-600 hover:bg-gray-100'
            "
            class="rounded-full px-3 py-1.5 text-xs font-medium"
            @click="statusFilter = status"
          >
            {{ status }}
          </button>
        </div>
      </section>

      <section
        v-if="viewMode === 'agenda'"
        class="overflow-x-auto rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6"
      >
        <table class="min-w-full rounded-2xl">
          <thead>
            <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
              <th class="py-3 text-start">Fecha</th>
              <th class="py-3 text-start">Hora</th>
              <th class="py-3 text-start">Cliente</th>
              <th class="py-3 text-start">Entrenador</th>
              <th class="py-3 text-start">Estado</th>
              <th class="py-3 text-end">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="session in filteredSessions"
              :key="session.id"
              class="cursor-pointer border-b border-gray-100 transition hover:bg-gray-50"
              @click="openDetail(session)"
            >
              <td class="py-4 text-sm font-medium text-gray-800">{{ session.fecha }}</td>
              <td class="py-4">
                <div class="flex items-center gap-2">
                  <span :class="statusDot(session.estado)" class="h-2 w-2 rounded-full"></span>
                  <span class="text-sm text-gray-800"
                    >{{ session.horaInicio }} - {{ session.horaFin }}</span
                  >
                </div>
              </td>
              <td class="py-4 text-sm text-gray-800">
                {{
                  memberById(session.clienteId)
                    ? nombreCompleto(memberById(session.clienteId)!)
                    : 'Cliente eliminado'
                }}
              </td>
              <td class="py-4 text-sm text-gray-600">
                {{
                  trainerById(session.entrenadorId)
                    ? nombreCompleto(trainerById(session.entrenadorId)!)
                    : 'Entrenador eliminado'
                }}
              </td>
              <td class="py-4">
                <Badge :color="statusColorName(session.estado)">● {{ session.estado }}</Badge>
              </td>
              <td class="py-4 text-end">
                <button
                  class="rounded-lg px-3 py-2 text-sm font-medium text-brand-600 transition hover:bg-brand-50 hover:text-brand-700"
                  @click.stop="openDetail(session)"
                >
                  Ver detalle
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <p v-if="!filteredSessions.length" class="p-8 text-center text-sm text-gray-500">
          No hay sesiones que coincidan con los filtros.
        </p>
      </section>

      <section
        v-else
        class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-theme-xs"
      >
        <div class="min-w-210">
          <div class="grid grid-cols-7 border-b border-gray-200">
            <div
              v-for="day in weekDays"
              :key="day.date"
              :class="day.isToday ? 'bg-brand-25 text-brand-600' : 'text-gray-500'"
              class="flex min-h-24 flex-col items-center justify-center border-e border-gray-200 px-2 py-4 text-center last:border-e-0"
            >
              <span class="text-xs font-semibold uppercase tracking-wider">{{ day.label }}</span>
              <strong
                class="mt-1 text-xl"
                :class="day.isToday ? 'text-brand-600' : 'text-gray-900'"
                >{{ day.number }}</strong
              >
              <span class="mt-1 text-xs text-gray-400"
                >{{ sessionsForDay(day.date).length }}
                {{ sessionsForDay(day.date).length === 1 ? 'sesión' : 'sesiones' }}</span
              >
            </div>
          </div>
          <div class="grid min-h-130 grid-cols-7">
            <div
              v-for="day in weekDays"
              :key="day.date"
              :class="day.isToday ? 'bg-brand-25/40' : 'bg-white'"
              class="flex min-h-130 flex-col gap-2 border-e border-gray-200 p-2 last:border-e-0 sm:p-3"
            >
              <button
                v-for="session in sessionsForDay(day.date)"
                :key="session.id"
                :class="weekSessionClass(session.estado)"
                class="group w-full rounded-xl border p-2.5 text-start transition duration-150 hover:scale-[1.02] hover:opacity-90"
                @click="openDetail(session)"
              >
                <span
                  :class="weekTimeClass(session.estado)"
                  class="block font-mono text-xs font-bold"
                  >{{ session.horaInicio }}</span
                >
                <span class="mt-1 block truncate text-xs font-semibold text-gray-800">{{
                  memberById(session.clienteId)
                    ? nombreCompleto(memberById(session.clienteId)!)
                    : 'Cliente eliminado'
                }}</span>
              </button>
              <button
                v-if="!sessionsForDay(day.date).length"
                class="flex flex-1 items-center justify-center text-2xl font-light text-gray-200 transition hover:text-brand-400"
                aria-label="Agregar sesión"
                @click="openCreateModal"
              >
                +
              </button>
              <button
                v-else
                class="mt-auto flex items-center justify-center py-2 text-lg font-light text-gray-200 transition hover:text-brand-400"
                aria-label="Agregar sesión"
                @click="openCreateModal"
              >
                +
              </button>
            </div>
          </div>
        </div>
      </section>
    </div>

    <Modal v-if="modal === 'create'" full-screen-backdrop @close="closeModal">
      <template #body>
        <div class="p-4">
          <form
            class="max-h-[90vh] w-full max-w-xl overflow-y-auto rounded-2xl bg-white p-6 shadow-theme-xl"
            @submit.prevent="createSession"
          >
            <div class="flex items-center justify-between">
              <h2 class="text-xl font-semibold text-gray-900">Nueva Sesión</h2>
              <button type="button" class="text-2xl text-gray-400" @click="closeModal">×</button>
            </div>
            <div class="mt-6 space-y-4">
              <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Cliente<select
                  v-model="form.clienteId"
                  required
                  class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"
                >
                  <option value="">Seleccionar cliente...</option>
                  <option v-for="member in activeMembers" :key="member.id" :value="member.id">
                    {{ nombreCompleto(member) }}
                  </option>
                </select></label
              >
              <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Entrenador<select
                  v-model="form.entrenadorId"
                  required
                  class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"
                >
                  <option value="">Seleccionar entrenador...</option>
                  <option v-for="trainer in activeTrainers" :key="trainer.id" :value="trainer.id">
                    {{ nombreCompleto(trainer) }} · ${{ trainer.tarifaPorSesion }}
                  </option>
                </select></label
              >
              <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Fecha<input
                  v-model="form.fecha"
                  required
                  type="date"
                  :min="fechaHoy()"
                  class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"
              /></label>
              <div class="grid grid-cols-2 gap-4">
                <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
                  >Hora de inicio<input
                    v-model="form.horaInicio"
                    required
                    type="time"
                    class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"
                /></label>
                <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
                  >Hora de fin<input
                    v-model="form.horaFin"
                    required
                    type="time"
                    class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"
                /></label>
              </div>
            </div>
            <Alert
              v-if="error"
              class="mt-5"
              variant="error"
              title="No se pudo agendar"
              :message="error"
            />
            <div class="mt-6 flex gap-3">
              <Button
                type="button"
                variant="outline"
                class-name="flex-1 justify-center"
                @click="closeModal"
                >Cancelar</Button
              >
              <Button type="submit" :disabled="guardando" class-name="flex-1 justify-center">{{
                guardando ? 'Agendando...' : 'Agendar Sesión'
              }}</Button>
            </div>
          </form>
        </div>
      </template>
    </Modal>

    <Modal v-if="modal === 'detail' && selectedSession" full-screen-backdrop @close="closeModal">
      <template #body>
        <div class="p-4">
          <section
            class="max-h-[90vh] w-full max-w-xl overflow-y-auto rounded-2xl bg-white shadow-theme-xl"
          >
            <header
              :class="detailHeader(selectedSession.estado)"
              class="rounded-t-2xl border-b border-gray-200 p-6"
            >
              <div class="flex items-start justify-between">
                <div>
                  <Badge :color="statusColorName(selectedSession.estado)"
                    >● {{ selectedSession.estado }}</Badge
                  >
                  <p class="mt-3 text-sm text-gray-500">
                    {{ selectedSession.fecha }} · {{ selectedSession.horaInicio }} -
                    {{ selectedSession.horaFin }}
                  </p>
                </div>
                <button class="text-2xl text-gray-400" @click="closeModal">×</button>
              </div>
            </header>
            <div class="space-y-5 p-6">
              <div class="grid grid-cols-2 gap-3">
                <div class="rounded-xl border border-gray-200 bg-gray-50 p-4">
                  <p class="text-xs text-gray-500">Cliente</p>
                  <p class="mt-2 font-semibold text-gray-800">
                    {{
                      memberById(selectedSession.clienteId)
                        ? nombreCompleto(memberById(selectedSession.clienteId)!)
                        : 'Cliente eliminado'
                    }}
                  </p>
                </div>
                <div class="rounded-xl border border-gray-200 bg-gray-50 p-4">
                  <p class="text-xs text-gray-500">Entrenador</p>
                  <p class="mt-2 font-semibold text-gray-800">
                    {{
                      trainerById(selectedSession.entrenadorId)
                        ? nombreCompleto(trainerById(selectedSession.entrenadorId)!)
                        : 'Entrenador eliminado'
                    }}
                  </p>
                </div>
              </div>
              <div v-if="nextActions.length" class="border-t border-gray-100 pt-5">
                <p class="mb-3 text-xs font-semibold uppercase tracking-wider text-gray-500">
                  Acciones
                </p>
                <div class="flex gap-3">
                  <button
                    v-for="action in nextActions"
                    :key="action.label"
                    :class="action.class"
                    class="flex-1 rounded-lg py-2.5 text-sm font-medium"
                    :disabled="guardando"
                    @click="runAction(action.estado)"
                  >
                    {{ action.label }}
                  </button>
                </div>
              </div>
              <Alert v-if="error" variant="error" title="No se pudo actualizar" :message="error" />
            </div>
          </section>
        </div>
      </template>
    </Modal>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'
import Modal from '@/components/ui/Modal.vue'
import Alert from '@/components/ui/Alert.vue'
import {
  useGymData,
  nombreCompleto,
  fechaHoy,
  type GymSession,
  type SessionStatus,
} from '@/composables/useGymData'

const {
  sessions,
  members,
  trainers,
  memberById,
  trainerById,
  addSession,
  updateSessionStatus,
} = useGymData()
const error = ref('')
const guardando = ref(false)
const viewMode = ref<'agenda' | 'week'>('agenda')
const search = ref('')
const statusFilter = ref('Todas')
const trainerFilter = ref('Todos')
const modal = ref<'create' | 'detail' | null>(null)
const selectedSession = ref<GymSession | null>(null)
const statuses = ['Todas', 'Programada', 'Confirmada', 'Completada', 'Cancelada']
const form = reactive({
  clienteId: '',
  entrenadorId: '',
  fecha: fechaHoy(),
  horaInicio: '09:00',
  horaFin: '10:00',
})
const activeMembers = computed(() => members.value.filter((member) => member.estado === 'Activo'))
const activeTrainers = computed(() =>
  trainers.value.filter((trainer) => trainer.estado === 'Activo'),
)
const filteredSessions = computed(() =>
  sessions.value
    .filter((session) => {
      const text =
        `${memberById(session.clienteId) ? nombreCompleto(memberById(session.clienteId)!) : ''} ${trainerById(session.entrenadorId) ? nombreCompleto(trainerById(session.entrenadorId)!) : ''}`.toLowerCase()
      return (
        (statusFilter.value === 'Todas' || session.estado === statusFilter.value) &&
        (trainerFilter.value === 'Todos' || session.entrenadorId === trainerFilter.value) &&
        text.includes(search.value.toLowerCase())
      )
    })
    .sort((a, b) => `${a.fecha}${a.horaInicio}`.localeCompare(`${b.fecha}${b.horaInicio}`)),
)
const statusSummary = computed(() => [
  {
    label: 'Programada',
    count: sessions.value.filter((session) => session.estado === 'Programada').length,
    dot: 'bg-blue-light-500',
    text: 'text-blue-light-600',
  },
  {
    label: 'Confirmada',
    count: sessions.value.filter((session) => session.estado === 'Confirmada').length,
    dot: 'bg-brand-500',
    text: 'text-brand-600',
  },
  {
    label: 'Completada',
    count: sessions.value.filter((session) => session.estado === 'Completada').length,
    dot: 'bg-teal-500',
    text: 'text-teal-600',
  },
  {
    label: 'Cancelada',
    count: sessions.value.filter((session) => session.estado === 'Cancelada').length,
    dot: 'bg-gray-500',
    text: 'text-gray-500',
  },
])
const nextActions = computed(() =>
  selectedSession.value?.estado === 'Programada'
    ? [
        {
          label: 'Confirmar',
          estado: 'Confirmada' as SessionStatus,
          class: 'bg-brand-100 text-brand-700',
        },
        {
          label: 'Cancelar sesión',
          estado: 'Cancelada' as SessionStatus,
          class: 'bg-error-100 text-error-600',
        },
      ]
    : selectedSession.value?.estado === 'Confirmada'
      ? [
          {
            label: 'Marcar completada',
            estado: 'Completada' as SessionStatus,
            class: 'bg-brand-100 text-brand-700',
          },
          {
            label: 'Cancelar sesión',
            estado: 'Cancelada' as SessionStatus,
            class: 'bg-error-100 text-error-600',
          },
        ]
      : [],
)
const weekDays = computed(() =>
  Array.from({ length: 7 }, (_, index) => {
    // Semana actual, de lunes a domingo
    const date = new Date()
    date.setDate(date.getDate() - ((date.getDay() + 6) % 7) + index)
    const iso = fechaHoy(date)
    return {
      date: iso,
      number: date.getDate(),
      label: date.toLocaleDateString('es-SV', { weekday: 'short' }),
      isToday: iso === fechaHoy(),
    }
  }),
)
const sessionsForDay = (date: string) =>
  filteredSessions.value.filter((session) => session.fecha === date)
const weekSessionClass = (estado: SessionStatus) =>
  ({
    Programada: 'border-blue-light-200 bg-blue-light-50',
    Confirmada: 'border-brand-200 bg-brand-50',
    Completada: 'border-teal-200 bg-teal-50',
    Cancelada: 'border-gray-200 bg-gray-50',
  })[estado]
const weekTimeClass = (estado: SessionStatus) =>
  ({
    Programada: 'text-blue-light-600',
    Confirmada: 'text-brand-600',
    Completada: 'text-teal-600',
    Cancelada: 'text-gray-500',
  })[estado]
const statusDot = (estado: SessionStatus) =>
  ({
    Programada: 'bg-blue-light-500',
    Confirmada: 'bg-brand-500',
    Completada: 'bg-teal-500',
    Cancelada: 'bg-gray-500',
  })[estado]
const statusColorName = (estado: SessionStatus) =>
  ({ Programada: 'info', Confirmada: 'primary', Completada: 'success', Cancelada: 'light' })[
    estado
  ] as 'info' | 'primary' | 'success' | 'light'
const detailHeader = (estado: SessionStatus) =>
  ({
    Programada: 'bg-blue-light-50/70',
    Confirmada: 'bg-brand-50/70',
    Completada: 'bg-teal-50/70',
    Cancelada: 'bg-gray-50',
  })[estado]
const mensajeError = (e: unknown) => (e instanceof Error ? e.message : 'Ocurrió un error inesperado.')
// El formulario se limpia cada vez que se abre, se haya guardado o cancelado antes
const openCreateModal = () => {
  form.clienteId = ''
  form.entrenadorId = ''
  form.fecha = fechaHoy()
  form.horaInicio = '09:00'
  form.horaFin = '10:00'
  error.value = ''
  modal.value = 'create'
}
const openDetail = (session: GymSession) => {
  error.value = ''
  selectedSession.value = session
  modal.value = 'detail'
}
const closeModal = () => {
  modal.value = null
  selectedSession.value = null
}
// Las reglas (horario, choques, activos) las valida el backend
const createSession = async () => {
  guardando.value = true
  error.value = ''
  try {
    await addSession({ ...form })
    closeModal()
  } catch (e) {
    error.value = mensajeError(e)
  } finally {
    guardando.value = false
  }
}
const runAction = async (estado: SessionStatus) => {
  if (!selectedSession.value) return
  guardando.value = true
  error.value = ''
  try {
    const id = selectedSession.value.id
    await updateSessionStatus(id, estado)
    selectedSession.value = sessions.value.find((session) => session.id === id) ?? null
  } catch (e) {
    error.value = mensajeError(e)
  } finally {
    guardando.value = false
  }
}
</script>
