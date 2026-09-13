<template>
  <AdminLayout>
    <div class="space-y-6">
      <header class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Clientes</h1>
          <p class="mt-1 text-sm text-gray-500">{{ members.length }} socios registrados</p>
        </div>
        <button
          v-if="canCreate"
          class="rounded-lg bg-brand-500 px-4 py-2.5 text-sm font-medium text-white hover:bg-brand-600"
          @click="openCreate"
        >
          + Nuevo Cliente
        </button>
      </header>
      <section>
        <div class="flex flex-col gap-3 lg:flex-row">
          <input
            v-model="search"
            class="h-11 flex-1 rounded-lg border border-gray-200 px-4 text-sm outline-none focus:border-brand-500"
            placeholder="Buscar por nombre o email..."
          />
          <div class="flex flex-wrap gap-2">
            <button
              v-for="status in statuses"
              :key="status"
              :class="filter === status ? 'bg-brand-500 text-white' : 'bg-gray-50 text-gray-600'"
              class="rounded-full border border-gray-200 px-4 py-2 text-sm font-medium transition"
              @click="filter = status"
            >
              {{ status }}
            </button>
          </div>
        </div>
        <div class="mt-5 overflow-x-auto rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6">
          <table class="min-w-full rounded-2xl">
            <thead>
              <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
                <th class="py-3 text-start">Cliente</th>
                <th class="py-3 text-start">Plan</th>
                <th class="py-3 text-start">Ingreso</th>
                <th class="py-3 text-start">Vencimiento</th>
                <th class="py-3 text-start">Asistencias</th>
                <th class="py-3 text-start">Estado</th>
                <th class="py-3 text-end">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="member in filteredMembers"
                :key="member.id"
                class="border-b border-gray-100 transition hover:bg-gray-50"
              >
                <td class="py-4">
                  <div class="flex items-center gap-3">
                    <span
                      class="flex h-10 w-10 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
                      >{{ member.initials }}</span
                    >
                    <div>
                      <p class="text-sm font-semibold text-gray-800">{{ member.name }}</p>
                      <p class="text-xs text-gray-500">{{ member.email }}</p>
                    </div>
                  </div>
                </td>
                <td class="py-4">
                  <span
                    :class="planClass(member.plan)"
                    class="rounded-full px-2.5 py-1 text-xs font-medium"
                    >{{ member.plan }}</span
                  >
                </td>
                <td class="py-4 font-mono text-sm text-gray-500">{{ member.startDate }}</td>
                <td class="py-4 text-sm text-gray-600">{{ member.expiry }}</td>
                <td class="py-4 text-sm font-semibold text-gray-800">{{ member.attendance.length * 20 + 62 }}</td>
                <td class="py-4">
                  <span
                    :class="badgeClass(member.status)"
                    class="rounded-full px-2.5 py-1 text-xs font-medium"
                    >{{ member.status }}</span
                  >
                </td>
                <td class="py-4 text-end">
                  <button
                    class="rounded-lg px-3 py-2 text-sm font-medium text-brand-600 transition hover:bg-brand-50 hover:text-brand-700"
                    @click.stop="router.push(`/clientes/${member.id}`)"
                  >
                    Ver perfil
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <p v-if="!filteredMembers.length" class="p-8 text-center text-sm text-gray-500">
            No hay clientes que coincidan con los filtros.
          </p>
        </div>
      </section>
    </div>
    <div
      v-if="modal"
      class="fixed inset-0 z-99999 flex items-center justify-center bg-gray-900/60 p-4 backdrop-blur-[2px]"
      @click.self="closeModal"
    >
      <form
        class="max-h-[90vh] w-full max-w-140 overflow-y-auto rounded-2xl bg-white shadow-theme-xl"
        @submit.prevent="createMember"
      >
        <header class="flex items-center justify-between border-b border-gray-200 p-6">
          <h2 class="text-xl font-semibold text-gray-900">Nuevo Cliente</h2>
          <button type="button" class="text-2xl text-gray-400" @click="closeModal">×</button>
        </header>
          <div class="grid grid-cols-1 gap-4 p-6 sm:grid-cols-2">
          <label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
            >Nombre completo<input
              v-model="form.name"
              required
              placeholder="Valentina Ríos"
              class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
          ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
            >Email<input
              v-model="form.email"
              required
              type="email"
              placeholder="valeria@email.com"
              class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
          ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
            >Teléfono<input
              v-model="form.phone"
              required
              placeholder="+54 11 0000-0000"
              class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
          ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
            >Fecha de nacimiento<input
              v-model="form.birthDate"
              type="date"
              class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
          ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
            >Dirección<input
              v-model="form.address"
              placeholder="Av. Corrientes 1234, CABA"
              class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
          ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
            >Plan de suscripción<select
              v-model="form.plan"
              required
              class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
            >
              <option v-for="plan in plans" :key="plan.id" :value="plan.name">
                {{ plan.name }} · ${{ plan.price.toLocaleString('es-MX') }}
              </option>
            </select></label
          ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
            >Notas iniciales<textarea
              v-model="form.notes"
              rows="3"
              placeholder="Objetivos, restricciones médicas, observaciones..."
              class="mt-2 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 py-2 text-sm"
            ></textarea>
          </label>
        </div>
        <footer class="flex gap-3 border-t border-gray-100 p-6">
          <button
            type="button"
            class="flex-1 rounded-xl border border-gray-200 py-2.5 text-sm font-medium text-gray-600"
            @click="closeModal"
          >
            Cancelar</button
          ><button class="flex-1 rounded-xl bg-brand-500 py-2.5 text-sm font-semibold text-white hover:bg-brand-600">
            Registrar cliente
          </button>
        </footer>
      </form>
    </div>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useAuth } from '@/composables/useAuth'
import { useGymData, type MemberStatus } from '@/composables/useGymData'

const router = useRouter()
const { currentUser } = useAuth()
const { members, plans, addMember } = useGymData()
const search = ref('')
const filter = ref('Todos')
const modal = ref(false)
const statuses = ['Todos', 'Activo', 'Por vencer', 'Vencido', 'Inactivo']
const canCreate = computed(() => currentUser.value?.role !== 'Entrenador')
const form = reactive({
  name: '',
  email: '',
  phone: '',
  address: '',
  birthDate: '',
  plan: plans.value[0]?.name || '',
  notes: '',
})
const filteredMembers = computed(() =>
  members.value.filter(
    (member) =>
      (filter.value === 'Todos' || member.status === filter.value) &&
      `${member.name} ${member.email}`.toLowerCase().includes(search.value.toLowerCase()),
  ),
)
const badgeClass = (status: MemberStatus) =>
  ({
    Activo: 'bg-success-50 text-success-600',
    'Por vencer': 'bg-warning-50 text-warning-600',
    Vencido: 'bg-error-50 text-error-600',
    Inactivo: 'bg-gray-100 text-gray-500',
  })[status]
const planClass = (plan: string) => {
  const color = plans.value.find((item) => item.name === plan)?.color
  return (
    (
      {
        blue: 'bg-blue-light-50 text-blue-light-600',
        lime: 'bg-success-50 text-success-600',
        orange: 'bg-orange-50 text-orange-600',
        violet: 'bg-purple-50 text-purple-600',
        red: 'bg-error-50 text-error-600',
      } as Record<string, string>
    )[color || ''] || 'bg-gray-100 text-gray-500'
  )
}
const openCreate = () => {
  form.plan = plans.value[0]?.name || ''
  modal.value = true
}
const closeModal = () => {
  modal.value = false
}
const createMember = () => {
  const plan = plans.value.find((item) => item.name === form.plan)
  if (!plan) return
  addMember({
    id: `m${Date.now()}`,
    name: form.name,
    email: form.email,
    phone: form.phone,
    plan: plan.name,
    startDate: '2026-07-14',
    expiry: plan.duration === '12 meses' ? '2027-07-14' : '2026-08-14',
    status: 'Activo',
    trainer: '',
    initials: form.name
      .split(' ')
      .map((part) => part[0])
      .join('')
      .slice(0, 2)
      .toUpperCase(),
    attendance: [],
    address: form.address,
    birthDate: form.birthDate,
    notes: form.notes,
    qrCode: `PU-${Date.now()}`,
  })
  closeModal()
}
</script>
