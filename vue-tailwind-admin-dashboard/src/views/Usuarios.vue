<template>
  <AdminLayout
    ><div class="space-y-6">
      <header class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Accesos del sistema</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Usuarios</h1>
          <p class="mt-1 text-sm text-gray-500">{{ users.length }} usuarios registrados</p>
        </div>
        <div class="flex gap-3">
          <input
            v-model="search"
            class="h-11 rounded-lg border border-gray-200 px-4 text-sm outline-none focus:border-brand-500"
            placeholder="Buscar por nombre o usuario..."
          /><Button size="sm" @click="openCreate">+ Nuevo Usuario</Button>
        </div>
      </header>
      <section
        class="overflow-x-auto rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6"
      >
        <table class="min-w-full rounded-2xl">
          <thead>
            <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
              <th class="py-3 text-start">Usuario</th>
              <th class="py-3 text-start">Nombre de usuario</th>
              <th class="py-3 text-start">Rol</th>
              <th class="py-3 text-start">Estado</th>
              <th class="py-3 text-end">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="user in filteredUsers"
              :key="user.id"
              class="border-b border-gray-100 transition hover:bg-gray-50"
            >
              <td class="py-4">
                <div class="flex items-center gap-3">
                  <span
                    class="flex h-10 w-10 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
                    >{{ user.initials }}</span
                  >
                  <p class="text-sm font-semibold text-gray-800">
                    {{ user.nombres }} {{ user.apellidos }}
                  </p>
                </div>
              </td>
              <td class="py-4 font-mono text-sm text-gray-500">{{ user.nombreUsuario }}</td>
              <td class="py-4">
                <Badge :color="roleColor(user.role)">{{ user.role }}</Badge>
              </td>
              <td class="py-4">
                <Badge color="primary">Activo</Badge>
              </td>
              <td class="py-4 text-end">
                <button
                  class="rounded-lg px-3 py-2 text-sm font-medium text-brand-600 transition hover:bg-brand-50 hover:text-brand-700"
                  @click="openEdit(user)"
                >
                  Editar
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <p v-if="!filteredUsers.length" class="p-8 text-center text-sm text-gray-500">
          No hay usuarios que coincidan con la búsqueda.
        </p>
      </section>
    </div>
    <Modal v-if="modal" full-screen-backdrop @close="closeModal">
      <template #body>
        <div class="p-4">
          <form
            class="max-h-[90vh] w-full max-w-140 overflow-y-auto rounded-2xl bg-white shadow-theme-xl"
            @submit.prevent="createUser"
          >
            <header class="flex items-center justify-between border-b border-gray-200 p-6">
              <h2 class="text-xl font-semibold text-gray-900">
                {{
                  editingUser
                    ? `Editar — ${editingUser.nombres} ${editingUser.apellidos}`
                    : 'Nuevo Usuario'
                }}
              </h2>
              <button type="button" class="text-2xl text-gray-400" @click="closeModal">×</button>
            </header>
            <div class="grid grid-cols-1 gap-4 p-6 sm:grid-cols-2">
              <label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Nombres<input
                  v-model="form.nombres"
                  required
                  placeholder="Daniel Enrique"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Apellidos<input
                  v-model="form.apellidos"
                  required
                  placeholder="Guevara Gómez"
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
                  placeholder="01234567-9"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Email<input
                  v-model="form.email"
                  required
                  type="email"
                  placeholder="daniel.guevara@plusultra.com"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Teléfono<input
                  v-model="form.telefono"
                  required
                  placeholder="2257-8888"
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
                  placeholder="Colonia San Benito, San Salvador"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label
                class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
                >Rol<select
                  v-model="form.role"
                  required
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
                >
                  <option value="Administrador">Administrador</option>
                  <option value="Gerente">Gerente</option>
                  <option value="Recepcionista">Recepcionista</option>
                  <option value="Entrenador">Entrenador</option>
                </select></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Nombre de usuario<input
                  v-model="form.nombreUsuario"
                  required
                  placeholder="daniel_admin"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >{{ editingUser ? 'Nueva contraseña (opcional)' : 'Contraseña'
                }}<input
                  v-model="form.contrasena"
                  :required="!editingUser"
                  type="password"
                  :placeholder="editingUser ? 'Dejar en blanco para no cambiarla' : ''"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm"
              /></label>
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
                editingUser ? 'Guardar cambios' : 'Registrar usuario'
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
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'
import Modal from '@/components/ui/Modal.vue'
import { useAuth, type UserRole, type SessionUser } from '@/composables/useAuth'
import type { Genero } from '@/composables/useGymData'
const { demoUsers } = useAuth()
const users = ref<SessionUser[]>(demoUsers.map(({ password: _password, ...user }) => user))
const search = ref('')
const filteredUsers = computed(() =>
  users.value.filter((user) =>
    `${user.nombres} ${user.apellidos} ${user.nombreUsuario}`
      .toLowerCase()
      .includes(search.value.toLowerCase()),
  ),
)
const modal = ref(false)
const editingUser = ref<SessionUser | null>(null)
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
  role: 'Recepcionista' as UserRole,
})
const roleColor = (role: UserRole) =>
  ({
    Administrador: 'warning',
    Gerente: 'purple',
    Recepcionista: 'info',
    Entrenador: 'primary',
  })[role] as 'warning' | 'purple' | 'info' | 'primary'
const resetForm = () => {
  form.nombres = ''
  form.apellidos = ''
  form.genero = 'Masculino'
  form.dui = ''
  form.email = ''
  form.telefono = ''
  form.fechaNacimiento = ''
  form.direccion = ''
  form.nombreUsuario = ''
  form.contrasena = ''
  form.role = 'Recepcionista'
}
const openCreate = () => {
  editingUser.value = null
  resetForm()
  modal.value = true
}
const openEdit = (user: SessionUser) => {
  editingUser.value = user
  form.nombres = user.nombres
  form.apellidos = user.apellidos
  form.genero = user.genero
  form.dui = user.dui
  form.email = user.email
  form.telefono = user.telefono
  form.fechaNacimiento = user.fechaNacimiento
  form.direccion = user.direccion
  form.nombreUsuario = user.nombreUsuario
  form.contrasena = ''
  form.role = user.role
  modal.value = true
}
const closeModal = () => {
  modal.value = false
  editingUser.value = null
}
const createUser = () => {
  const data = {
    nombreUsuario: form.nombreUsuario,
    nombres: form.nombres,
    apellidos: form.apellidos,
    genero: form.genero,
    fechaNacimiento: form.fechaNacimiento,
    direccion: form.direccion,
    email: form.email,
    telefono: form.telefono,
    dui: form.dui,
    role: form.role,
    initials: `${form.nombres.charAt(0)}${form.apellidos.charAt(0)}`.toUpperCase(),
  }
  if (editingUser.value) {
    Object.assign(editingUser.value, data)
  } else {
    users.value.unshift({ id: `${Date.now()}`, ...data })
  }
  closeModal()
}
</script>
