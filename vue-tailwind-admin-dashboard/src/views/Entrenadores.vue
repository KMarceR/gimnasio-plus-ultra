<template>
  <AdminLayout
    ><div class="space-y-6">
      <header class="flex items-end justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Equipo operativo</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Entrenadores</h1>
        </div>
        <Button v-if="canCreate" size="sm" @click="openCreate">+ Nuevo Entrenador</Button>
      </header>
      <section class="overflow-x-auto rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6">
        <table class="min-w-full rounded-2xl">
          <thead>
            <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
              <th class="py-3 text-start">Entrenador</th>
              <th class="py-3 text-start">Especialidad</th>
              <th class="py-3 text-start">Tarifa por sesión</th>
              <th class="py-3 text-start">Usuario</th>
              <th class="py-3 text-start">Estado</th>
              <th class="py-3 text-end">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="trainer in trainers"
              :key="trainer.id"
              class="border-b border-gray-100 transition hover:bg-gray-50"
            >
              <td class="py-4">
                <div class="flex items-center gap-3">
                  <span
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
                    >{{ iniciales(trainer) }}</span
                  >
                  <p class="text-sm font-semibold text-gray-800">{{ nombreCompleto(trainer) }}</p>
                </div>
              </td>
              <td class="py-4 text-sm text-gray-600">{{ trainer.especialidad }}</td>
              <td class="py-4 text-sm font-semibold text-gray-800">${{ trainer.tarifaPorSesion }}</td>
              <td class="py-4 font-mono text-sm text-gray-500">{{ trainer.nombreUsuario }}</td>
              <td class="py-4">
                <Badge :color="trainer.estado === 'Activo' ? 'primary' : 'light'">{{ trainer.estado }}</Badge>
              </td>
              <td class="py-4 text-end">
                <RouterLink
                  :to="`/entrenadores/${trainer.id}`"
                  class="rounded-lg px-3 py-2 text-sm font-medium text-brand-600 transition hover:bg-brand-50 hover:text-brand-700"
                  >Ver perfil</RouterLink
                >
              </td>
            </tr>
          </tbody>
        </table>
        <p v-if="!trainers.length" class="p-8 text-center text-sm text-gray-500">No hay entrenadores registrados.</p>
      </section>
    </div>
    <Modal v-if="modal" full-screen-backdrop @close="closeModal">
      <template #body>
        <div class="p-4">
          <form
            class="max-h-[90vh] w-full max-w-140 overflow-y-auto rounded-2xl bg-white shadow-theme-xl"
            @submit.prevent="createTrainer"
          >
            <header class="flex items-center justify-between border-b border-gray-200 p-6">
              <h2 class="text-xl font-semibold text-gray-900">Nuevo Entrenador</h2>
              <button type="button" class="text-2xl text-gray-400" @click="closeModal">×</button>
            </header>
            <div class="grid grid-cols-1 gap-4 p-6 sm:grid-cols-2">
              <label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Nombres<input
                  v-model="form.nombres"
                  required
                  placeholder="Nelson Eduardo"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Apellidos<input
                  v-model="form.apellidos"
                  required
                  placeholder="Palacios Henríquez"
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
                  placeholder="04891234-5"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Email<input
                  v-model="form.email"
                  required
                  type="email"
                  placeholder="nelson.trainer@gmail.com"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Teléfono<input
                  v-model="form.telefono"
                  required
                  placeholder="7745-9988"
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
                  placeholder="Urbanización Prados de Venecia, Soyapango"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Usuario<input
                  v-model="form.nombreUsuario"
                  required
                  placeholder="nelson_coach"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Contraseña<input
                  v-model="form.contrasena"
                  required
                  type="password"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label
                class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
                >Especialidad<input
                  v-model="form.especialidad"
                  required
                  placeholder="Culturismo, HIIT y Nutrición Funcional"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
                >Tarifa por sesión ($)<input
                  v-model.number="form.tarifaPorSesion"
                  required
                  min="1"
                  type="number"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              >
            </div>
            <footer class="flex gap-3 border-t border-gray-100 p-6">
              <Button type="button" variant="outline" class-name="flex-1 justify-center" @click="closeModal"
                >Cancelar</Button
              >
              <Button type="submit" class-name="flex-1 justify-center">Registrar entrenador</Button>
            </footer>
          </form>
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
import { useAuth } from '@/composables/useAuth'
import { useGymData, nombreCompleto, iniciales, type Genero } from '@/composables/useGymData'

const { currentUser } = useAuth()
const { trainers, addTrainer } = useGymData()
const modal = ref(false)
const canCreate = computed(() => currentUser.value?.role === 'Administrador' || currentUser.value?.role === 'Gerente')
const form = reactive({
  nombres: '',
  apellidos: '',
  genero: 'Masculino' as Genero,
  dui: '',
  email: '',
  telefono: '',
  fechaNacimiento: '',
  direccion: '',
  nombreUsuario: '',
  contrasena: '',
  especialidad: '',
  tarifaPorSesion: 0,
})
const openCreate = () => {
  modal.value = true
}
const closeModal = () => {
  modal.value = false
}
const createTrainer = () => {
  addTrainer({
    id: `${Date.now()}`,
    nombres: form.nombres,
    apellidos: form.apellidos,
    genero: form.genero,
    fechaNacimiento: form.fechaNacimiento,
    direccion: form.direccion,
    email: form.email,
    telefono: form.telefono,
    dui: form.dui,
    nombreUsuario: form.nombreUsuario,
    especialidad: form.especialidad,
    tarifaPorSesion: Number(form.tarifaPorSesion),
    estado: 'Activo',
  })
  closeModal()
}
</script>
