<template>
  <AdminLayout>
    <div class="space-y-6">
      <header class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Agenda operativa</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Control de Sesiones</h1>
          <p class="mt-1 text-sm text-gray-500">{{ sessions.length }} sesiones registradas</p>
        </div>
        <button class="rounded-lg bg-brand-500 px-4 py-2.5 text-sm font-medium text-white hover:bg-brand-600" @click="openCreateModal">
          + Nueva Sesión
        </button>
      </header>

      <section class="grid grid-cols-2 gap-4 xl:grid-cols-4">
        <article v-for="metric in metrics" :key="metric.label" class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs">
          <div class="flex items-center justify-between gap-3">
            <p class="text-xs font-semibold uppercase tracking-wider text-gray-500">{{ metric.label }}</p>
            <span :class="metric.class" class="flex h-9 w-9 items-center justify-center rounded-full text-sm">{{ metric.icon }}</span>
          </div>
          <p class="mt-4 text-3xl font-semibold text-gray-900">{{ metric.value }}</p>
        </article>
      </section>

      <section class="flex flex-wrap items-center gap-x-6 gap-y-2 text-sm text-gray-500">
        <span v-for="status in statusSummary" :key="status.label" class="inline-flex items-center gap-2">
          <span :class="status.dot" class="h-2.5 w-2.5 rounded-full"></span>
          <strong :class="status.text">{{ status.count }}</strong> {{ status.label }}
        </span>
      </section>

      <section class="rounded-2xl border border-gray-200 bg-white p-4 shadow-theme-xs sm:p-5">
        <div class="flex flex-col gap-3 xl:flex-row xl:items-center">
          <div class="flex rounded-lg bg-gray-100 p-1">
            <button :class="viewMode === 'agenda' ? 'bg-white text-gray-900 shadow-theme-xs' : 'text-gray-500'" class="rounded-md px-4 py-2 text-sm font-medium" @click="viewMode = 'agenda'">Agenda</button>
            <button :class="viewMode === 'week' ? 'bg-white text-gray-900 shadow-theme-xs' : 'text-gray-500'" class="rounded-md px-4 py-2 text-sm font-medium" @click="viewMode = 'week'">Semana</button>
          </div>
          <input v-model="search" class="h-10 min-w-0 flex-1 rounded-lg border border-gray-200 px-3 text-sm outline-none focus:border-brand-500" placeholder="Buscar cliente, entrenador o tipo..." />
          <select v-model="trainerFilter" class="h-10 rounded-lg border border-gray-200 px-3 text-sm text-gray-700 outline-none focus:border-brand-500">
            <option value="Todos">Todos los entrenadores</option>
            <option v-for="trainer in activeTrainers" :key="trainer.id" :value="trainer.id">{{ trainer.name }}</option>
          </select>
        </div>
        <div class="mt-3 flex flex-wrap gap-2">
          <button v-for="status in statuses" :key="status" :class="statusFilter === status ? 'bg-brand-500 text-white' : 'bg-gray-50 text-gray-600 hover:bg-gray-100'" class="rounded-full px-3 py-1.5 text-xs font-medium" @click="statusFilter = status">{{ status }}</button>
        </div>
      </section>

      <section v-if="viewMode === 'agenda'" class="space-y-6">
        <div v-for="group in groupedSessions" :key="group.date">
          <div class="mb-3 flex items-center gap-3 text-xs font-semibold uppercase tracking-wider text-gray-500">
            <span>{{ group.date }}</span><span class="h-px flex-1 bg-gray-200"></span><span>{{ group.items.length }} sesiones</span>
          </div>
          <div class="space-y-3">
            <button v-for="session in group.items" :key="session.id" class="flex w-full flex-col gap-4 rounded-2xl border border-gray-200 bg-white p-4 text-start shadow-theme-xs transition hover:border-brand-200 hover:shadow-theme-sm sm:flex-row sm:items-center" @click="openDetail(session)">
              <span :class="statusColor(session.status)" class="h-12 w-1 rounded-full sm:h-14"></span>
              <span class="w-20 shrink-0"><strong class="block text-lg text-gray-900">{{ session.time }}</strong><small class="text-xs text-gray-500">{{ session.duration }} min</small></span>
              <span class="min-w-0 flex-1"><strong class="block truncate text-sm text-gray-900">{{ memberById(session.memberId)?.name }} <span class="font-normal text-gray-400">· {{ session.type }}</span></strong><small class="mt-1 block text-xs text-gray-500">⚒ {{ trainerById(session.trainerId)?.name }}</small></span>
              <span :class="statusBadge(session.status)" class="w-fit rounded-full px-2.5 py-1 text-xs font-medium">● {{ session.status }}</span>
              <span class="text-sm font-semibold text-gray-700">${{ session.rate.toLocaleString('es-MX') }}</span><span class="text-xl text-gray-400">›</span>
            </button>
          </div>
        </div>
        <p v-if="!groupedSessions.length" class="rounded-2xl border border-dashed border-gray-300 bg-white p-10 text-center text-sm text-gray-500">No hay sesiones que coincidan con los filtros.</p>
      </section>

      <section v-else class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-theme-xs">
        <div class="min-w-210">
          <div class="grid grid-cols-7 border-b border-gray-200">
            <div v-for="day in weekDays" :key="day.date" :class="day.isToday ? 'bg-brand-25 text-brand-600' : 'text-gray-500'" class="flex min-h-24 flex-col items-center justify-center border-e border-gray-200 px-2 py-4 text-center last:border-e-0">
              <span class="text-xs font-semibold uppercase tracking-wider">{{ day.label }}</span>
              <strong class="mt-1 text-xl" :class="day.isToday ? 'text-brand-600' : 'text-gray-900'">{{ day.number }}</strong>
              <span class="mt-1 text-xs text-gray-400">{{ sessionsForDay(day.date).length }} {{ sessionsForDay(day.date).length === 1 ? 'sesión' : 'sesiones' }}</span>
            </div>
          </div>
          <div class="grid min-h-130 grid-cols-7">
            <div v-for="day in weekDays" :key="day.date" :class="day.isToday ? 'bg-brand-25/40' : 'bg-white'" class="flex min-h-130 flex-col gap-2 border-e border-gray-200 p-2 last:border-e-0 sm:p-3">
              <button v-for="session in sessionsForDay(day.date)" :key="session.id" :class="weekSessionClass(session.status)" class="group w-full rounded-xl border p-2.5 text-start transition duration-150 hover:scale-[1.02] hover:opacity-90" @click="openDetail(session)">
                <span :class="weekTimeClass(session.status)" class="block font-mono text-xs font-bold">{{ session.time }}</span>
                <span class="mt-1 block truncate text-xs font-semibold text-gray-800">{{ memberById(session.memberId)?.name }}</span>
                <span class="mt-0.5 block truncate text-[11px] text-gray-500">{{ session.type }}</span>
              </button>
              <button v-if="!sessionsForDay(day.date).length" class="flex flex-1 items-center justify-center text-2xl font-light text-gray-200 transition hover:text-brand-400" aria-label="Agregar sesión" @click="openCreateModal">+</button>
              <button v-else class="mt-auto flex items-center justify-center py-2 text-lg font-light text-gray-200 transition hover:text-brand-400" aria-label="Agregar sesión" @click="openCreateModal">+</button>
            </div>
          </div>
        </div>
      </section>
    </div>

    <div v-if="modal === 'create'" class="fixed inset-0 z-99999 flex items-center justify-center bg-gray-900/50 p-4" @click.self="closeModal">
      <form class="max-h-[90vh] w-full max-w-xl overflow-y-auto rounded-2xl bg-white p-6 shadow-theme-xl" @submit.prevent="createSession">
        <div class="flex items-center justify-between"><h2 class="text-xl font-semibold text-gray-900">Nueva Sesión</h2><button type="button" class="text-2xl text-gray-400" @click="closeModal">×</button></div>
        <div class="mt-6 space-y-4"><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500">Cliente<select v-model="form.memberId" required class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"><option value="">Seleccionar cliente...</option><option v-for="member in activeMembers" :key="member.id" :value="member.id">{{ member.name }} · {{ member.plan }}</option></select></label><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500">Entrenador<select v-model="form.trainerId" required class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"><option value="">Seleccionar entrenador...</option><option v-for="trainer in activeTrainers" :key="trainer.id" :value="trainer.id">{{ trainer.name }} · ${{ trainer.rate }}</option></select></label><div class="grid grid-cols-2 gap-4"><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500">Fecha<input v-model="form.date" required type="date" class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm" /></label><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500">Hora<input v-model="form.time" required type="time" class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm" /></label><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500">Duración<select v-model.number="form.duration" class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"><option v-for="duration in durations" :key="duration" :value="duration">{{ duration }} min</option></select></label><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500">Tipo de sesión<select v-model="form.type" class="mt-2 h-11 w-full rounded-lg border border-gray-200 px-3 text-sm"><option v-for="type in sessionTypes" :key="type">{{ type }}</option></select></label></div><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500">Notas opcionales<textarea v-model="form.notes" rows="3" class="mt-2 w-full rounded-lg border border-gray-200 px-3 py-2 text-sm" placeholder="Objetivos, indicaciones, observaciones..."></textarea></label></div><div class="mt-6 flex gap-3"><button type="button" class="flex-1 rounded-lg border border-gray-200 py-2.5 text-sm font-medium text-gray-700" @click="closeModal">Cancelar</button><button class="flex-1 rounded-lg bg-brand-500 py-2.5 text-sm font-medium text-white hover:bg-brand-600">Agendar Sesión</button></div>
      </form>
    </div>

    <div v-if="modal === 'detail' && selectedSession" class="fixed inset-0 z-99999 flex items-center justify-center bg-gray-900/50 p-4" @click.self="closeModal">
      <section class="max-h-[90vh] w-full max-w-xl overflow-y-auto rounded-2xl bg-white shadow-theme-xl"><header :class="detailHeader(selectedSession.status)" class="rounded-t-2xl border-b border-gray-200 p-6"><div class="flex items-start justify-between"><div><span :class="statusBadge(selectedSession.status)" class="rounded-full px-2.5 py-1 text-xs font-medium">● {{ selectedSession.status }}</span><h2 class="mt-4 text-xl font-semibold text-gray-900">{{ selectedSession.type }}</h2><p class="mt-1 text-sm text-gray-500">{{ selectedSession.date }} · {{ selectedSession.time }} · {{ selectedSession.duration }} min</p></div><button class="text-2xl text-gray-400" @click="closeModal">×</button></div></header><div class="space-y-5 p-6"><div class="grid grid-cols-2 gap-3"><div class="rounded-xl border border-gray-200 bg-gray-50 p-4"><p class="text-xs text-gray-500">Cliente</p><p class="mt-2 font-semibold text-gray-800">{{ memberById(selectedSession.memberId)?.name }}</p></div><div class="rounded-xl border border-gray-200 bg-gray-50 p-4"><p class="text-xs text-gray-500">Entrenador</p><p class="mt-2 font-semibold text-gray-800">{{ trainerById(selectedSession.trainerId)?.name }}</p></div></div><div class="grid grid-cols-3 gap-3 text-center"><div class="rounded-xl border border-gray-200 bg-gray-50 p-3"><p class="text-xs text-gray-500">Duración</p><p class="mt-1 text-sm font-semibold text-gray-800">{{ selectedSession.duration }} min</p></div><div class="rounded-xl border border-gray-200 bg-gray-50 p-3"><p class="text-xs text-gray-500">Tipo</p><p class="mt-1 truncate text-sm font-semibold text-gray-800">{{ selectedSession.type }}</p></div><div class="rounded-xl border border-gray-200 bg-gray-50 p-3"><p class="text-xs text-gray-500">Tarifa</p><p class="mt-1 text-sm font-semibold text-gray-800">${{ selectedSession.rate.toLocaleString('es-MX') }}</p></div></div><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500">Notas de la sesión<textarea v-model="detailNotes" rows="3" class="mt-2 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 py-3 text-sm text-gray-700 outline-none focus:border-brand-500"></textarea></label><button v-if="detailNotes !== selectedSession.notes" class="text-sm font-medium text-brand-600" @click="saveNotes">Guardar notas</button><div v-if="nextActions.length" class="border-t border-gray-100 pt-5"><p class="mb-3 text-xs font-semibold uppercase tracking-wider text-gray-500">Acciones</p><div class="flex gap-3"><button v-for="action in nextActions" :key="action.label" :class="action.class" class="flex-1 rounded-lg py-2.5 text-sm font-medium" @click="runAction(action.status)">{{ action.label }}</button></div></div><button class="w-full border-t border-gray-100 pt-5 text-sm font-medium text-error-600" @click="removeSelected">Eliminar sesión</button></div></section>
    </div>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useGymData, type GymSession, type SessionStatus } from '@/composables/useGymData'

const { sessions, members, trainers, memberById, trainerById, addSession, updateSessionStatus, updateSessionNotes, deleteSession } = useGymData()
const viewMode = ref<'agenda' | 'week'>('agenda')
const search = ref('')
const statusFilter = ref('Todas')
const trainerFilter = ref('Todos')
const modal = ref<'create' | 'detail' | null>(null)
const selectedSession = ref<GymSession | null>(null)
const detailNotes = ref('')
const statuses = ['Todas', 'Programada', 'Confirmada', 'Completada', 'Cancelada']
const durations = [30, 45, 60, 75, 90, 120]
const sessionTypes = ['Musculación', 'HIIT', 'Cardio', 'Powerlifting', 'CrossFit', 'Pilates', 'Funcional', 'Rehabilitación', 'Técnica', 'Flexibilidad', 'Otro']
const form = reactive({ memberId: '', trainerId: '', date: '2026-07-17', time: '09:00', duration: 60, type: 'Musculación', notes: '' })
const activeMembers = computed(() => members.value.filter((member) => member.status === 'Activo'))
const activeTrainers = computed(() => trainers.value.filter((trainer) => trainer.active))
const filteredSessions = computed(() => sessions.value.filter((session) => { const text = `${memberById(session.memberId)?.name} ${trainerById(session.trainerId)?.name} ${session.type}`.toLowerCase(); return (statusFilter.value === 'Todas' || session.status === statusFilter.value) && (trainerFilter.value === 'Todos' || session.trainerId === trainerFilter.value) && text.includes(search.value.toLowerCase()) }).sort((a, b) => `${a.date}${a.time}`.localeCompare(`${b.date}${b.time}`)))
const groupedSessions = computed(() => { const groups = new Map<string, GymSession[]>(); filteredSessions.value.forEach((session) => groups.set(session.date, [...(groups.get(session.date) || []), session])); return [...groups.entries()].map(([date, items]) => ({ date, items })) })
const metrics = computed(() => [{ label: 'Sesiones hoy', value: sessions.value.filter((session) => session.date === '2026-07-14').length, icon: '▣', class: 'bg-blue-light-50 text-blue-light-600' }, { label: 'Esta semana', value: sessions.value.length, icon: '◷', class: 'bg-success-50 text-success-600' }, { label: 'Completadas', value: sessions.value.filter((session) => session.status === 'Completada').length, icon: '✓', class: 'bg-success-50 text-success-600' }, { label: 'Ingresos (sesiones)', value: `$${sessions.value.reduce((total, session) => total + session.rate, 0).toLocaleString('es-MX')}`, icon: '↗', class: 'bg-orange-50 text-orange-600' }])
const statusSummary = computed(() => [{ label: 'Programada', count: sessions.value.filter((session) => session.status === 'Programada').length, dot: 'bg-blue-light-500', text: 'text-blue-light-600' }, { label: 'Confirmada', count: sessions.value.filter((session) => session.status === 'Confirmada').length, dot: 'bg-success-500', text: 'text-success-600' }, { label: 'Completada', count: sessions.value.filter((session) => session.status === 'Completada').length, dot: 'bg-teal-500', text: 'text-teal-600' }, { label: 'Cancelada', count: sessions.value.filter((session) => session.status === 'Cancelada').length, dot: 'bg-gray-500', text: 'text-gray-500' }])
const nextActions = computed(() => selectedSession.value?.status === 'Programada' ? [{ label: 'Confirmar', status: 'Confirmada' as SessionStatus, class: 'bg-success-100 text-success-700' }, { label: 'Cancelar sesión', status: 'Cancelada' as SessionStatus, class: 'bg-error-100 text-error-600' }] : selectedSession.value?.status === 'Confirmada' ? [{ label: 'Marcar completada', status: 'Completada' as SessionStatus, class: 'bg-success-100 text-success-700' }, { label: 'Cancelar sesión', status: 'Cancelada' as SessionStatus, class: 'bg-error-100 text-error-600' }] : [])
const weekDays = computed(() => Array.from({ length: 7 }, (_, index) => {
  const date = new Date('2026-07-14T12:00:00')
  date.setDate(date.getDate() + index)
  const iso = date.toISOString().slice(0, 10)
  return {
    date: iso,
    number: date.getDate(),
    label: date.toLocaleDateString('es-MX', { weekday: 'short' }),
    isToday: iso === '2026-07-14',
  }
}))
const sessionsForDay = (date: string) => filteredSessions.value.filter((session) => session.date === date)
const weekSessionClass = (status: SessionStatus) => ({
  Programada: 'border-blue-light-200 bg-blue-light-50',
  Confirmada: 'border-success-200 bg-success-50',
  Completada: 'border-teal-200 bg-teal-50',
  Cancelada: 'border-gray-200 bg-gray-50',
})[status]
const weekTimeClass = (status: SessionStatus) => ({
  Programada: 'text-blue-light-600',
  Confirmada: 'text-success-600',
  Completada: 'text-teal-600',
  Cancelada: 'text-gray-500',
})[status]
const statusColor = (status: SessionStatus) => ({ Programada: 'bg-blue-light-500', Confirmada: 'bg-success-500', Completada: 'bg-teal-500', Cancelada: 'bg-gray-500' })[status]
const statusBadge = (status: SessionStatus) => ({ Programada: 'bg-blue-light-50 text-blue-light-600', Confirmada: 'bg-success-50 text-success-600', Completada: 'bg-teal-50 text-teal-600', Cancelada: 'bg-gray-100 text-gray-500' })[status]
const detailHeader = (status: SessionStatus) => ({ Programada: 'bg-blue-light-50/70', Confirmada: 'bg-success-50/70', Completada: 'bg-teal-50/70', Cancelada: 'bg-gray-50' })[status]
const openCreateModal = () => { modal.value = 'create' }
const openDetail = (session: GymSession) => { selectedSession.value = session; detailNotes.value = session.notes; modal.value = 'detail' }
const closeModal = () => { modal.value = null; selectedSession.value = null }
const createSession = () => { const trainer = trainerById(form.trainerId); if (!trainer) return; addSession({ id: `s${Date.now()}`, memberId: form.memberId, trainerId: form.trainerId, date: form.date, time: form.time, duration: form.duration, type: form.type, status: 'Programada', rate: Math.round(trainer.rate * form.duration / 60), notes: form.notes }); closeModal() }
const runAction = (status: SessionStatus) => { if (selectedSession.value) { updateSessionStatus(selectedSession.value.id, status); selectedSession.value.status = status } }
const saveNotes = () => { if (selectedSession.value) { updateSessionNotes(selectedSession.value.id, detailNotes.value) } }
const removeSelected = () => { if (selectedSession.value) { deleteSession(selectedSession.value.id); closeModal() } }
</script>
