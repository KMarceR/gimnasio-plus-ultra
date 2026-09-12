<template>
  <AdminLayout>
    <div v-if="selectedPlan" class="space-y-6">
      <div class="flex items-center justify-between">
        <button class="text-sm font-medium text-brand-600" @click="selectedPlan = null">
          ← Volver a Suscripciones
        </button>
        <div class="flex gap-2">
          <button
            class="rounded-lg border border-gray-200 px-3 py-2 text-sm font-medium text-gray-700"
            @click="openEdit(selectedPlan)"
          >
            Editar plan</button
          ><button
            v-if="!selectedPlan.isBase"
            class="rounded-lg border border-error-200 px-3 py-2 text-sm font-medium text-error-600"
            @click="removePlan(selectedPlan)"
          >
            Eliminar
          </button>
        </div>
      </div>
      <section
        :class="planTheme(selectedPlan.color).border"
        class="rounded-2xl border bg-white p-6 shadow-theme-xs sm:p-8"
      >
        <div class="flex flex-col gap-6 sm:flex-row sm:items-start">
          <span
            :class="planTheme(selectedPlan.color).soft"
            class="flex h-14 w-14 items-center justify-center rounded-2xl text-2xl"
            ><CreditCard class="h-7 w-7" :stroke-width="1.8"
          /></span>
          <div class="flex-1">
            <div class="flex flex-wrap items-center gap-2">
              <span
                :class="planTheme(selectedPlan.color).text"
                class="text-sm font-semibold uppercase"
                >{{ selectedPlan.duration }}</span
              ><span
                class="rounded-full bg-success-50 px-2.5 py-1 text-xs font-medium text-success-600"
                >{{ membersForPlan(selectedPlan.name).length }} socios activos</span
              >
            </div>
            <h1 class="mt-2 text-3xl font-semibold text-gray-900">{{ selectedPlan.name }}</h1>
            <p :class="planTheme(selectedPlan.color).text" class="mt-3 text-4xl font-bold">
              ${{ selectedPlan.price.toLocaleString('es-MX')
              }}<small class="text-base font-normal text-gray-500">
                / {{ selectedPlan.duration }}</small
              >
            </p>
          </div>
          <div class="text-end">
            <p class="text-xs text-gray-500">Facturación mensual</p>
            <p :class="planTheme(selectedPlan.color).text" class="mt-1 text-2xl font-bold">
              ${{
                (membersForPlan(selectedPlan.name).length * selectedPlan.price).toLocaleString(
                  'es-MX',
                )
              }}
            </p>
          </div>
        </div>
        <div class="mt-8 grid grid-cols-1 gap-3 border-t border-gray-100 pt-6 sm:grid-cols-2">
          <p v-for="feature in selectedPlan.features" :key="feature" class="text-sm text-gray-600">
            <span :class="planTheme(selectedPlan.color).text">✓</span> {{ feature }}
          </p>
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
            >{{ member.initials }}</span
          >
          <div class="flex-1">
            <p class="text-sm font-semibold text-gray-800">{{ member.name }}</p>
            <p class="text-xs text-gray-500">{{ member.email }}</p>
          </div>
          <p class="hidden text-sm text-gray-500 sm:block">Vence {{ member.expiry }}</p>
          <span
            :class="
              member.status === 'Vencido'
                ? 'bg-error-50 text-error-600'
                : member.status === 'Por vencer'
                  ? 'bg-warning-50 text-warning-600'
                  : 'bg-success-50 text-success-600'
            "
            class="rounded-full px-2.5 py-1 text-xs font-medium"
            >{{ member.status }}</span
          >
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
          /><button
            class="rounded-lg bg-brand-500 px-4 py-2.5 text-sm font-medium text-white hover:bg-brand-600"
            @click="openCreate"
          >
            + Nuevo Plan
          </button>
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
      <section class="space-y-4">
        <article
          v-for="plan in filteredPlans"
          :key="plan.id"
          :class="planTheme(plan.color).border"
          class="group cursor-pointer rounded-2xl border bg-white p-5 shadow-theme-xs transition duration-200 hover:-translate-y-1 hover:bg-gray-50/70 hover:shadow-theme-lg hover:ring-2 hover:ring-brand-500/10"
          @click="openDetail(plan)"
        >
          <div class="flex flex-col gap-4 md:flex-row md:items-center">
            <div class="flex min-w-55 items-center gap-4">
              <span
                :class="planTheme(plan.color).soft"
                class="flex h-11 w-11 items-center justify-center rounded-xl transition duration-200 group-hover:scale-110"
                ><CreditCard class="h-5 w-5" :stroke-width="1.8" /></span
              >
              <div>
                <h2 class="text-lg font-semibold text-gray-900">{{ plan.name }}</h2>
                <p :class="planTheme(plan.color).text" class="text-sm font-medium">
                  {{ plan.duration }}
                </p>
              </div>
            </div>
            <div class="md:w-36">
              <p class="text-xs text-gray-500">Precio</p>
              <p :class="planTheme(plan.color).text" class="mt-1 text-xl font-semibold">
                ${{ plan.price.toLocaleString('es-MX') }}
              </p>
            </div>
            <div class="flex-1">
              <div class="flex justify-between text-xs text-gray-500">
                <span>{{ membersForPlan(plan.name).length }} socios</span
                ><span>{{ plan.features.length * 10 }}%</span>
              </div>
              <div class="mt-2 h-2 rounded-full bg-gray-100">
                <div
                  :class="planTheme(plan.color).bar"
                  class="h-2 rounded-full"
                  :style="{ width: `${Math.min(plan.features.length * 10, 100)}%` }"
                ></div>
              </div>
            </div>
            <div class="md:w-36">
              <p class="text-xs text-gray-500">Facturación</p>
              <p class="mt-1 font-semibold text-gray-800">
                ${{ (membersForPlan(plan.name).length * plan.price).toLocaleString('es-MX') }}
              </p>
            </div>
            <div class="min-w-44 text-sm text-gray-600">
              <p v-for="feature in plan.features.slice(0, 2)" :key="feature">
                <span :class="planTheme(plan.color).text">✓</span> {{ feature }}
              </p>
              <p v-if="plan.features.length > 2" class="text-xs text-gray-400">
                +{{ plan.features.length - 2 }} más
              </p>
            </div>
            <div class="flex items-center gap-2 md:ms-auto">
              <button
                class="rounded-lg p-2 text-gray-400 transition hover:bg-gray-100"
                title="Editar plan"
                @click.stop="openEdit(plan)"
              >
                ✎</button
              ><button
                class="whitespace-nowrap rounded-lg px-3 py-2 text-sm font-medium text-brand-600 opacity-70 transition hover:bg-brand-50 hover:text-brand-700 group-hover:opacity-100"
                @click.stop="openDetail(plan)"
              >
                Ver detalle ›
              </button>
            </div>
          </div>
        </article>
      </section>
      <p
        v-if="!filteredPlans.length"
        class="rounded-2xl border border-dashed border-gray-300 bg-white p-10 text-center text-sm text-gray-500"
      >
        No hay planes que coincidan con la búsqueda.
      </p>
    </div>

    <div
      v-if="modal"
      class="fixed inset-0 z-99999 flex items-center justify-center bg-gray-900/50 p-4"
      @click.self="closeModal"
    >
      <form
        class="max-h-[90vh] w-full max-w-xl overflow-y-auto rounded-2xl bg-white shadow-theme-xl"
        @submit.prevent="savePlan"
      >
        <header class="flex items-center justify-between border-b border-gray-200 p-6">
          <h2 class="text-xl font-semibold text-gray-900">
            {{ editingPlan ? `Editar — ${editingPlan.name}` : 'Nuevo Plan' }}
          </h2>
          <button type="button" class="text-2xl text-gray-400" @click="closeModal">×</button>
        </header>
        <div class="space-y-5 p-6">
          <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
            >Nombre del plan<input
              v-model="form.name"
              required
              class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
              placeholder="Ej: Semestral Elite"
          /></label>
          <div class="grid grid-cols-2 gap-4">
            <label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
              >Precio ($)<input
                v-model.number="form.price"
                required
                min="1"
                type="number"
                class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
            ><label class="block text-xs font-semibold uppercase tracking-wider text-gray-500"
              >Duración<select
                v-model="form.duration"
                class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
              >
                <option>1 mes</option>
                <option>3 meses</option>
                <option>6 meses</option>
                <option>12 meses</option>
              </select></label
            >
          </div>
          <div>
            <p class="text-xs font-semibold uppercase tracking-wider text-gray-500">
              Color del plan
            </p>
            <div class="mt-2 grid grid-cols-5 gap-2">
              <button
                v-for="color in colors"
                :key="color.value"
                type="button"
                :class="
                  form.color === color.value
                    ? `${planTheme(color.value).selected} ${planTheme(color.value).text}`
                    : 'border-gray-200 text-gray-500'
                "
                class="rounded-full border px-3 py-2 text-xs font-medium"
                @click="form.color = color.value"
              >
                {{ color.label }}
              </button>
            </div>
          </div>
          <div>
            <p class="text-xs font-semibold uppercase tracking-wider text-gray-500">
              Beneficios incluidos
            </p>
            <div class="mt-2 flex gap-2">
              <input
                v-model="benefitInput"
                class="h-11 flex-1 rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
                placeholder="Ej: Acceso 24/7"
                @keyup.enter.prevent="addBenefit"
              /><button
                type="button"
                class="h-11 w-11 rounded-xl border border-success-200 bg-success-50 text-success-600"
                @click="addBenefit"
              >
                +
              </button>
            </div>
            <p v-if="!form.features.length" class="mt-2 text-xs italic text-gray-400">
              Agregá al menos un beneficio
            </p>
            <div
              v-for="(benefit, index) in form.features"
              :key="benefit"
              class="mt-2 flex items-center justify-between rounded-xl bg-gray-50 px-3 py-2 text-sm text-gray-700"
            >
              <span><span class="text-success-600">✓</span> {{ benefit }}</span
              ><button type="button" class="text-gray-400" @click="removeBenefit(index)">×</button>
            </div>
          </div>
          <div :class="planTheme(form.color).soft" class="rounded-xl border p-4">
            <p class="text-xs text-gray-500">Vista previa</p>
            <p :class="planTheme(form.color).text" class="mt-1 text-lg font-semibold">
              {{ form.name || 'Nombre del plan' }}
            </p>
            <p class="font-semibold text-gray-800">
              ${{ Number(form.price || 0).toLocaleString('es-MX') }} / {{ form.duration }}
            </p>
          </div>
          <p v-if="error" class="text-sm text-error-600">{{ error }}</p>
        </div>
        <footer class="flex gap-3 border-t border-gray-100 p-6">
          <button
            type="button"
            class="flex-1 rounded-lg border border-gray-200 py-2.5 text-sm font-medium text-gray-700"
            @click="closeModal"
          >
            Cancelar</button
          ><button class="flex-1 rounded-lg bg-brand-500 py-2.5 text-sm font-medium text-white">
            {{ editingPlan ? 'Guardar cambios' : 'Crear Plan' }}
          </button>
        </footer>
      </form>
    </div>
  </AdminLayout>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { CreditCard } from 'lucide-vue-next'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useGymData, type GymPlan, type PlanColor } from '@/composables/useGymData'

const { plans, members, addPlan, updatePlan, deletePlan } = useGymData()
const search = ref('')
const selectedPlan = ref<GymPlan | null>(null)
const memberSearch = ref('')
const modal = ref(false)
const editingPlan = ref<GymPlan | null>(null)
const benefitInput = ref('')
const error = ref('')
const form = reactive<{
  name: string
  price: number
  duration: string
  color: PlanColor
  features: string[]
}>({ name: '', price: 0, duration: '1 mes', color: 'blue', features: [] })
const colors: Array<{ label: string; value: PlanColor }> = [
  { label: 'Azul', value: 'blue' },
  { label: 'Lima', value: 'lime' },
  { label: 'Naranja', value: 'orange' },
  { label: 'Violeta', value: 'violet' },
  { label: 'Rojo', value: 'red' },
]
const filteredPlans = computed(() =>
  plans.value.filter((plan) =>
    `${plan.name} ${plan.duration} ${plan.price}`
      .toLowerCase()
      .includes(search.value.toLowerCase()),
  ),
)
const activeMembers = computed(
  () =>
    members.value.filter((member) => member.status === 'Activo' || member.status === 'Por vencer')
      .length,
)
const stats = computed(() => [
  {
    label: 'Ingresos estimados',
    value: `$${plans.value.reduce((total, plan) => total + membersForPlan(plan.name).length * plan.price, 0).toLocaleString('es-MX')}`,
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
const membersForPlan = (name: string) => members.value.filter((member) => member.plan === name)
const planMembers = computed(() =>
  selectedPlan.value
    ? membersForPlan(selectedPlan.value.name).filter((member) =>
        `${member.name} ${member.email}`.toLowerCase().includes(memberSearch.value.toLowerCase()),
      )
    : [],
)
const planTheme = (color: PlanColor) =>
  ({
    blue: {
      text: 'text-blue-light-600',
      border: 'border-blue-light-200',
      soft: 'bg-blue-light-50 border-blue-light-200',
      selected: 'border-blue-light-300 bg-blue-light-50',
      bar: 'bg-blue-light-500',
    },
    lime: {
      text: 'text-success-600',
      border: 'border-success-200',
      soft: 'bg-success-50 border-success-200',
      selected: 'border-success-300 bg-success-50',
      bar: 'bg-success-500',
    },
    orange: {
      text: 'text-orange-600',
      border: 'border-orange-200',
      soft: 'bg-orange-50 border-orange-200',
      selected: 'border-orange-300 bg-orange-50',
      bar: 'bg-orange-500',
    },
    violet: {
      text: 'text-theme-purple-500',
      border: 'border-purple-200',
      soft: 'bg-purple-50 border-purple-200',
      selected: 'border-purple-300 bg-purple-50',
      bar: 'bg-theme-purple-500',
    },
    red: {
      text: 'text-error-600',
      border: 'border-error-200',
      soft: 'bg-error-50 border-error-200',
      selected: 'border-error-300 bg-error-50',
      bar: 'bg-error-500',
    },
  })[color]
const resetForm = () => {
  form.name = ''
  form.price = 0
  form.duration = '1 mes'
  form.color = 'blue'
  form.features = []
  benefitInput.value = ''
  error.value = ''
}
const openCreate = () => {
  editingPlan.value = null
  resetForm()
  modal.value = true
}
const openEdit = (plan: GymPlan) => {
  editingPlan.value = plan
  form.name = plan.name
  form.price = plan.price
  form.duration = plan.duration
  form.color = plan.color
  form.features = [...plan.features]
  error.value = ''
  modal.value = true
}
const closeModal = () => {
  modal.value = false
  editingPlan.value = null
}
const addBenefit = () => {
  const value = benefitInput.value.trim()
  if (value && !form.features.includes(value)) form.features.push(value)
  benefitInput.value = ''
}
const removeBenefit = (index: number) => form.features.splice(index, 1)
const savePlan = () => {
  if (!form.features.length) {
    error.value = 'Agrega al menos un beneficio.'
    return
  }
  const data = {
    name: form.name.trim(),
    price: Number(form.price),
    duration: form.duration,
    color: form.color,
    features: [...form.features],
  }
  if (editingPlan.value) updatePlan(editingPlan.value.id, data)
  else addPlan({ ...data, id: `p${Date.now()}`, isBase: false })
  closeModal()
}
const openDetail = (plan: GymPlan) => {
  selectedPlan.value = plan
  memberSearch.value = ''
}
const removePlan = (plan: GymPlan) => {
  if (plan.isBase) return
  deletePlan(plan.id)
  selectedPlan.value = null
}
</script>
