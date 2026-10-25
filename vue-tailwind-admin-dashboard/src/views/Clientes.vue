<template>
  <AdminLayout>
    <div class="space-y-6">
      <header class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Clientes</h1>
          <p class="mt-1 text-sm text-gray-500">{{ members.length }} socios registrados</p>
        </div>
        <Button v-if="canCreate" size="sm" @click="openCreate">+ Nuevo Cliente</Button>
      </header>
      <section>
        <div class="flex flex-col gap-3 lg:flex-row">
          <input
            v-model="search"
            class="h-11 flex-1 rounded-lg border border-gray-200 px-4 text-sm outline-none focus:border-brand-500"
            placeholder="Buscar por nombre, DUI o email..."
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
        <div
          class="mt-5 overflow-x-auto rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6"
        >
          <table class="min-w-full rounded-2xl">
            <thead>
              <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
                <th class="py-3 text-start">Cliente</th>
                <th class="py-3 text-start">DUI</th>
                <th class="py-3 text-start">Plan</th>
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
                      >{{ iniciales(member) }}</span
                    >
                    <div>
                      <p class="text-sm font-semibold text-gray-800">
                        {{ nombreCompleto(member) }}
                      </p>
                      <p class="text-xs text-gray-500">{{ member.email }}</p>
                    </div>
                  </div>
                </td>
                <td class="py-4 font-mono text-sm text-gray-500">{{ member.dui }}</td>
                <td class="py-4">
                  <Badge color="info">{{ planById(member.planId)?.nombre || 'Sin plan' }}</Badge>
                </td>
                <td class="py-4 text-sm font-semibold text-gray-800">
                  {{ ingresosDeCliente(member.id) }}
                </td>
                <td class="py-4">
                  <Badge :color="statusColor(member.estado)">{{ member.estado }}</Badge>
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
    <Modal v-if="modal" full-screen-backdrop @close="closeModal">
      <template #body>
        <div class="p-4">
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
                >Nombres<input
                  v-model="form.nombres"
                  required
                  placeholder="Roberto Antonio"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Apellidos<input
                  v-model="form.apellidos"
                  required
                  placeholder="Zelaya Flores"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Género<select
                  v-model="form.genero"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
                >
                  <option value="Masculino">Masculino</option>
                  <option value="Femenino">Femenino</option>
                </select></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >DUI<input
                  v-model="form.dui"
                  required
                  pattern="\d{8}-\d"
                  maxlength="10"
                  title="Formato 00000000-0"
                  placeholder="02564718-9"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Email<input
                  v-model="form.email"
                  required
                  type="email"
                  placeholder="roberto.zelaya@outlook.com"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Teléfono<input
                  v-model="form.telefono"
                  required
                  pattern="\d{4}-\d{4}"
                  maxlength="9"
                  title="Formato 0000-0000"
                  placeholder="6102-3344"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Fecha de nacimiento<input
                  v-model="form.fechaNacimiento"
                  required
                  type="date"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label
                class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
                >Dirección<input
                  v-model="form.direccion"
                  placeholder="Colonia Utila, Santa Tecla, La Libertad"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label
                class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
                >Plan de suscripción<select
                  v-model="form.planId"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
                >
                  <option value="">Sin plan</option>
                  <option v-for="plan in planesActivos" :key="plan.id" :value="plan.id">
                    {{ plan.nombre }} · ${{ plan.precio.toLocaleString('es-SV') }}
                  </option>
                </select></label
              ><label
                class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
                >Notas<textarea
                  v-model="form.notas"
                  rows="3"
                  placeholder="Lesiones, objetivos, observaciones..."
                  class="mt-2 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 py-2 text-sm"
                ></textarea>
              </label>
            </div>
            <div v-if="error" class="px-6 pb-2">
              <Alert variant="error" title="No se pudo registrar" :message="error" />
            </div>
            <footer class="flex gap-3 border-t border-gray-100 p-6">
              <Button
                type="button"
                variant="outline"
                class-name="flex-1 justify-center"
                @click="closeModal"
                >Cancelar</Button
              >
              <Button type="submit" :disabled="guardando" class-name="flex-1 justify-center">{{
                guardando ? 'Guardando...' : 'Registrar cliente'
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
import { useRouter } from 'vue-router'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'
import Modal from '@/components/ui/Modal.vue'
import Alert from '@/components/ui/Alert.vue'
import { useAuth } from '@/composables/useAuth'
import {
  useGymData,
  nombreCompleto,
  iniciales,
  type EstadoGeneral,
  type Genero,
} from '@/composables/useGymData'

const router = useRouter()
const { currentUser } = useAuth()
const { members, plans, planById, ingresosDeCliente, addMember } = useGymData()
const search = ref('')
const filter = ref('Todos')
const modal = ref(false)
const error = ref('')
const guardando = ref(false)
const statuses = ['Todos', 'Activo', 'Inactivo']
const canCreate = computed(() => currentUser.value?.role !== 'Entrenador')
const form = reactive({
  nombres: '',
  apellidos: '',
  genero: 'Masculino' as Genero,
  dui: '',
  email: '',
  telefono: '',
  fechaNacimiento: '',
  direccion: '',
  planId: '',
  notas: '',
})
const planesActivos = computed(() => plans.value.filter((plan) => plan.estado === 'Activo'))
const filteredMembers = computed(() =>
  members.value.filter(
    (member) =>
      (filter.value === 'Todos' || member.estado === filter.value) &&
      `${nombreCompleto(member)} ${member.email} ${member.dui}`
        .toLowerCase()
        .includes(search.value.toLowerCase()),
  ),
)
const statusColor = (estado: EstadoGeneral) =>
  ({
    Activo: 'primary',
    Inactivo: 'light',
  })[estado] as 'primary' | 'light'
const openCreate = () => {
  Object.assign(form, {
    nombres: '',
    apellidos: '',
    genero: 'Masculino',
    dui: '',
    email: '',
    telefono: '',
    fechaNacimiento: '',
    direccion: '',
    planId: planesActivos.value[0]?.id || '',
    notas: '',
  })
  error.value = ''
  modal.value = true
}
const closeModal = () => {
  modal.value = false
}
// Si se registra activo y con plan, el backend le vende su primera suscripcion y genera su QR
const createMember = async () => {
  guardando.value = true
  error.value = ''
  try {
    await addMember({
      nombres: form.nombres,
      apellidos: form.apellidos,
      genero: form.genero,
      fechaNacimiento: form.fechaNacimiento,
      direccion: form.direccion,
      email: form.email,
      telefono: form.telefono,
      dui: form.dui,
      planId: form.planId || null,
      estado: 'Activo',
      notas: form.notas,
    })
    closeModal()
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'No se pudo registrar el cliente.'
  } finally {
    guardando.value = false
  }
}
</script>
