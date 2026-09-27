<template>
  <AdminLayout>
    <div v-if="selectedPlan" class="space-y-6">
      <div class="flex items-center justify-between">
        <button class="text-sm font-medium text-brand-600" @click="selectedPlan = null">
          ← Volver a Suscripciones
        </button>
        <div class="flex gap-2">
          <Button size="sm" variant="outline" @click="openEdit(selectedPlan)">Editar plan</Button>
          <Button
            size="sm"
            variant="outline"
            class-name="!ring-error-200 !text-error-600 hover:!bg-error-50"
            @click="removePlan(selectedPlan)"
          >
            Eliminar
          </Button>
        </div>
      </div>
      <section class="rounded-2xl border border-brand-200 bg-white p-6 shadow-theme-xs sm:p-8">
        <div class="flex flex-col gap-6 sm:flex-row sm:items-start">
          <span class="flex h-14 w-14 items-center justify-center rounded-2xl bg-brand-50 text-2xl"
            ><CreditCard class="h-7 w-7 text-brand-600" :stroke-width="1.8"
          /></span>
          <div class="flex-1">
            <div class="flex flex-wrap items-center gap-2">
              <Badge :color="selectedPlan.estado === 'Activo' ? 'primary' : 'light'">{{
                selectedPlan.estado
              }}</Badge>
              <span class="rounded-full bg-brand-50 px-2.5 py-1 text-xs font-medium text-brand-600"
                >{{ membersForPlan(selectedPlan.id).length }} socios</span
              >
            </div>
            <h1 class="mt-2 text-3xl font-semibold text-gray-900">{{ selectedPlan.nombre }}</h1>
            <p class="mt-3 max-w-xl text-sm text-gray-600">{{ selectedPlan.detalles }}</p>
          </div>
          <div class="text-end">
            <p class="text-xs text-gray-500">Precio</p>
            <p class="mt-1 text-3xl font-bold text-brand-600">
              ${{ selectedPlan.precio.toLocaleString('es-SV') }}
            </p>
          </div>
        </div>
      </section>
      <section class="rounded-2xl border border-gray-200 bg-white shadow-theme-xs">
        <div
          class="flex flex-col gap-3 border-b border-gray-100 p-5 sm:flex-row sm:items-center sm:justify-between"
        >
          <h2 class="font-semibold text-gray-900">Socios en este plan</h2>
          <input
            v-model="memberSearch"
            class="h-10 rounded-lg border border-gray-200 px-3 text-sm"
            placeholder="Buscar por nombre o email..."
          />
        </div>
        <div
          v-for="member in planMembers"
          :key="member.id"
          class="flex items-center gap-3 border-b border-gray-100 p-4 last:border-0"
        >
          <span
            class="flex h-10 w-10 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
            >{{ iniciales(member) }}</span
          >
          <div class="flex-1">
            <p class="text-sm font-semibold text-gray-800">{{ nombreCompleto(member) }}</p>
            <p class="text-xs text-gray-500">{{ member.email }}</p>
          </div>
          <Badge :color="member.estado === 'Activo' ? 'primary' : 'light'">{{
            member.estado
          }}</Badge>
        </div>
        <p v-if="!planMembers.length" class="p-8 text-center text-sm text-gray-500">
          No hay socios asignados a este plan.
        </p>
      </section>
    </div>

    <div v-else class="space-y-6">
      <header class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Planes y membresías</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Suscripciones</h1>
          <p class="mt-1 text-sm text-gray-500">
            {{ plans.length }} planes · {{ members.length }} socios · {{ activeMembers }} activos
          </p>
        </div>
        <div class="flex gap-3">
          <input
            v-model="search"
            class="h-11 rounded-lg border border-gray-200 px-4 text-sm"
            placeholder="Buscar plan..."
          /><Button size="sm" @click="openCreate">+ Nuevo Plan</Button>
        </div>
      </header>
      <section class="grid grid-cols-1 gap-4 lg:grid-cols-3">
        <article
          v-for="stat in stats"
          :key="stat.label"
          class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs"
        >
          <p class="text-sm text-gray-500">{{ stat.label }}</p>
          <p class="mt-2 text-2xl font-semibold text-gray-900">{{ stat.value }}</p>
          <p class="mt-1 text-xs text-gray-500">{{ stat.caption }}</p>
        </article>
      </section>
      <section
        class="overflow-x-auto rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6"
      >
        <table class="min-w-full rounded-2xl">
          <thead>
            <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
              <th class="py-3 text-start">Plan</th>
              <th class="py-3 text-start">Precio</th>
              <th class="py-3 text-start">Socios</th>
              <th class="py-3 text-start">Estado</th>
              <th class="py-3 text-end">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="plan in filteredPlans"
              :key="plan.id"
              class="cursor-pointer border-b border-gray-100 transition hover:bg-gray-50"
              @click="openDetail(plan)"
            >
              <td class="py-4">
                <div class="flex items-center gap-3">
                  <span
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-brand-50"
                    ><CreditCard class="h-5 w-5 text-brand-600" :stroke-width="1.8"
                  /></span>
                  <div class="min-w-0">
                    <p class="text-sm font-semibold text-gray-800">{{ plan.nombre }}</p>
                    <p class="max-w-xs truncate text-xs text-gray-500">{{ plan.detalles }}</p>
                  </div>
                </div>
              </td>
              <td class="py-4 text-sm font-semibold text-brand-600">
                ${{ plan.precio.toLocaleString('es-SV') }}
              </td>
              <td class="py-4 text-sm font-semibold text-gray-800">
                {{ membersForPlan(plan.id).length }}
              </td>
              <td class="py-4">
                <Badge :color="plan.estado === 'Activo' ? 'primary' : 'light'">{{
                  plan.estado
                }}</Badge>
              </td>
              <td class="py-4 text-end">
                <button
                  class="rounded-lg px-3 py-2 text-sm font-medium text-gray-500 transition hover:bg-gray-100"
                  title="Editar plan"
                  @click.stop="openEdit(plan)"
                >
                  Editar
                </button>
                <button
                  class="rounded-lg px-3 py-2 text-sm font-medium text-brand-600 transition hover:bg-brand-50 hover:text-brand-700"
                  @click.stop="openDetail(plan)"
                >
                  Ver detalle
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <p v-if="!filteredPlans.length" class="p-8 text-center text-sm text-gray-500">
          No hay planes que coincidan con la búsqueda.
        </p>
      </section>
    </div>

    <Modal v-if="modal" full-screen-backdrop @close="closeModal">
      <template #body>
        <div class="p-4">
          <form
            class="max-h-[90vh] w-full max-w-xl overflow-y-auto rounded-2xl bg-white shadow-theme-xl"
            @submit.prevent="savePlan"
          >
            <header class="flex items-center justify-between border-b border-gray-200 p-6">
              <h2 class="text-xl font-semibold text-gray-900">
                {{ editingPlan ? `Editar — ${editingPlan.nombre}` : 'Nuevo Plan' }}
              </h2>
              <button type="button" class="text-2xl text-gray-400" @click="closeModal">×</button>
            </header>
            <div class="space-y-5 p-6">
              <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Nombre del plan<input
                  v-model="form.nombre"
                  required
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
                  placeholder="Membresía Plus Ultra Black"
              /></label>
              <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Precio ($)<input
                  v-model.number="form.precio"
                  required
                  min="1"
                  type="number"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
              /></label>
              <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Detalles<textarea
                  v-model="form.detalles"
                  rows="3"
                  required
                  placeholder="Acceso completo ilimitado a zona de pesas, cardio, spinning..."
                  class="mt-2 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 py-2 text-sm"
                ></textarea>
              </label>
              <label
                class="flex items-center gap-3 text-xs font-semibold uppercase tracking-wider text-gray-500"
              >
                <input
                  v-model="form.activo"
                  type="checkbox"
                  class="h-4 w-4 rounded border-gray-300"
                />
                Plan activo
              </label>
              <Alert v-if="error" variant="error" title="No se pudo guardar" :message="error" />
            </div>
            <footer class="flex gap-3 border-t border-gray-100 p-6">
              <Button
                type="button"
                variant="outline"
                class-name="flex-1 justify-center"
                @click="closeModal"
                >Cancelar</Button
              >
              <Button type="submit" class-name="flex-1 justify-center">{{
                editingPlan ? 'Guardar cambios' : 'Crear Plan'
              }}</Button>
            </footer>
          </form>
        </div>
      </template>
    </Modal>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { CreditCard } from 'lucide-vue-next'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'
import Modal from '@/components/ui/Modal.vue'
import Alert from '@/components/ui/Alert.vue'
import { useGymData, nombreCompleto, iniciales, type GymPlan } from '@/composables/useGymData'

const { plans, members, addPlan, updatePlan, deletePlan } = useGymData()
const search = ref('')
const selectedPlan = ref<GymPlan | null>(null)
const memberSearch = ref('')
const modal = ref(false)
const editingPlan = ref<GymPlan | null>(null)
const error = ref('')
const form = reactive({ nombre: '', precio: 0, detalles: '', activo: true })
const filteredPlans = computed(() =>
  plans.value.filter((plan) =>
    `${plan.nombre} ${plan.detalles}`.toLowerCase().includes(search.value.toLowerCase()),
  ),
)
const activeMembers = computed(
  () => members.value.filter((member) => member.estado === 'Activo').length,
)
const stats = computed(() => [
  {
    label: 'Ingresos estimados',
    value: `$${plans.value.reduce((total, plan) => total + membersForPlan(plan.id).length * plan.precio, 0).toLocaleString('es-SV')}`,
    caption: 'por periodo',
  },
  {
    label: 'Socios activos',
    value: activeMembers.value,
    caption: `de ${members.value.length} totales`,
  },
  {
    label: 'Planes disponibles',
    value: plans.value.length,
    caption: `${filteredPlans.value.length} en vista`,
  },
])
const membersForPlan = (id: string) => members.value.filter((member) => member.planId === id)
const planMembers = computed(() =>
  selectedPlan.value
    ? membersForPlan(selectedPlan.value.id).filter((member) =>
        `${nombreCompleto(member)} ${member.email}`
          .toLowerCase()
          .includes(memberSearch.value.toLowerCase()),
      )
    : [],
)
const resetForm = () => {
  form.nombre = ''
  form.precio = 0
  form.detalles = ''
  form.activo = true
  error.value = ''
}
const openCreate = () => {
  editingPlan.value = null
  resetForm()
  modal.value = true
}
const openEdit = (plan: GymPlan) => {
  editingPlan.value = plan
  form.nombre = plan.nombre
  form.precio = plan.precio
  form.detalles = plan.detalles
  form.activo = plan.estado === 'Activo'
  error.value = ''
  modal.value = true
}
const closeModal = () => {
  modal.value = false
  editingPlan.value = null
}
const savePlan = () => {
  const data = {
    nombre: form.nombre.trim(),
    precio: Number(form.precio),
    detalles: form.detalles.trim(),
    estado: form.activo ? ('Activo' as const) : ('Inactivo' as const),
  }
  if (editingPlan.value) updatePlan(editingPlan.value.id, data)
  else addPlan({ ...data, id: `${Date.now()}` })
  closeModal()
}
const openDetail = (plan: GymPlan) => {
  selectedPlan.value = plan
  memberSearch.value = ''
}
const removePlan = (plan: GymPlan) => {
  deletePlan(plan.id)
  selectedPlan.value = null
}
</script>
