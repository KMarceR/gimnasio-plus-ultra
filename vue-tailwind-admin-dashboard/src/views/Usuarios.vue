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
      <Alert v-if="errorCarga" variant="error" title="No se pudieron cargar los usuarios" :message="errorCarga" />
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
                <Badge :color="user.estado === 'Activo' ? 'primary' : 'light'">{{ user.estado }}</Badge>
              </td>
              <td class="py-4 text-end">
                <button
                  class="rounded-lg px-3 py-2 text-sm font-medium text-brand-600 transition hover:bg-brand-50 hover:text-brand-700"
                  @click="editar(user)"
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
            @submit.prevent="guardar"
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
                  pattern="\d{8}-\d"
                  maxlength="10"
                  title="Formato 00000000-0"
                  placeholder="01234567-9"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Email<input
                  v-model="form.email"
                  required
                  type="email"
                  autocomplete="off"
                  placeholder="daniel.guevara@plusultra.com"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm" /></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Teléfono<input
                  v-model="form.telefono"
                  required
                  pattern="\d{4}-\d{4}"
                  maxlength="9"
                  title="Formato 0000-0000"
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
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Rol<select
                  v-model="form.idRol"
                  required
                  :disabled="esUsuarioConectado"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm disabled:opacity-60"
                >
                  <option v-for="rol in rolesDisponibles" :key="rol.idRol" :value="rol.idRol">
                    {{ rol.nombreRol }}
                  </option>
                </select></label
              ><label class="text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Estado<select
                  v-model="form.estado"
                  :disabled="esUsuarioConectado"
                  class="mt-2 h-11 w-full rounded-xl border border-gray-200 bg-gray-50 px-3 text-sm disabled:opacity-60"
                >
                  <option value="Activo">Activo</option>
                  <option value="Inactivo">Inactivo</option>
                </select></label
              ><p v-if="esUsuarioConectado" class="text-xs text-gray-500 sm:col-span-2">
                No puedes cambiar tu propio rol ni desactivarte; si lo hicieras, ya no podrías entrar
                al sistema.
              </p
              ><p class="text-xs text-gray-500 sm:col-span-2">
                El nombre de usuario es el DUI. Los entrenadores se registran en la pantalla
                Entrenadores.
              </p
              ><label
                class="text-xs font-semibold uppercase tracking-wider text-gray-500 sm:col-span-2"
                >{{ editingUser ? 'Nueva contraseña (opcional)' : 'Contraseña'
                }}<CampoContrasena
                  v-model="form.contrasena"
                  :required="!editingUser"
                  :placeholder="editingUser ? 'Déjala vacía para no cambiarla' : ''"
              /></label>
            </div>
            <div v-if="error" class="px-6 pb-2">
              <Alert variant="error" title="No se pudo guardar" :message="error" />
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
                guardando ? 'Guardando...' : editingUser ? 'Guardar cambios' : 'Registrar usuario'
              }}</Button>
            </footer>
          </form>
        </div>
      </template>
    </Modal>
  </AdminLayout>
</template>
<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Alert from '@/components/ui/Alert.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'
import CampoContrasena from '@/components/ui/CampoContrasena.vue'
import Modal from '@/components/ui/Modal.vue'
import {
  useAuth,
  aSessionUser,
  type UserRole,
  type SessionUser,
  type UsuarioApi,
} from '@/composables/useAuth'
import { useGymData, type EstadoGeneral, type Genero } from '@/composables/useGymData'
import { api } from '@/services/api'

interface Usuario extends SessionUser {
  estado: EstadoGeneral
}
interface RolApi {
  idRol: number
  nombreRol: UserRole
}

const { currentUser, guardarSesion } = useAuth()
const { idEstado, trainers } = useGymData()
const router = useRouter()
const users = ref<Usuario[]>([])
const roles = ref<RolApi[]>([])
const errorCarga = ref('')
const search = ref('')
const filteredUsers = computed(() =>
  users.value.filter((user) =>
    `${user.nombres} ${user.apellidos} ${user.nombreUsuario}`
      .toLowerCase()
      .includes(search.value.toLowerCase()),
  ),
)
const rolesDisponibles = computed(() => roles.value.filter((rol) => rol.nombreRol !== 'Entrenador'))
const modal = ref(false)
const editingUser = ref<Usuario | null>(null)
const esUsuarioConectado = computed(() => editingUser.value?.id === currentUser.value?.id)
const error = ref('')
const guardando = ref(false)
const form = reactive({
  nombres: '',
  apellidos: '',
  genero: 'Masculino' as Genero,
  dui: '',
  email: '',
  telefono: '',
  fechaNacimiento: '',
  direccion: '',
  contrasena: '',
  idRol: 0,
  estado: 'Activo' as EstadoGeneral,
})
const roleColor = (role: UserRole) =>
  ({
    Administrador: 'warning',
    Gerente: 'purple',
    Recepcionista: 'info',
    Entrenador: 'primary',
  })[role] as 'warning' | 'purple' | 'info' | 'primary'
const aUsuario = (usuario: UsuarioApi): Usuario => ({
  ...aSessionUser(usuario),
  estado: usuario.nombreEstado === 'Activo' ? 'Activo' : 'Inactivo',
})
const cargarUsuarios = async () => {
  users.value = (await api.get<UsuarioApi[]>('/usuarios')).map(aUsuario)
}
onMounted(async () => {
  try {
    const [listaRoles] = await Promise.all([api.get<RolApi[]>('/roles'), cargarUsuarios()])
    roles.value = listaRoles
  } catch (err) {
    errorCarga.value = err instanceof Error ? err.message : 'Error al cargar'
  }
})
const idRolPorNombre = (nombre: UserRole) =>
  roles.value.find((rol) => rol.nombreRol === nombre)?.idRol ?? 0
const resetForm = () => {
  form.nombres = ''
  form.apellidos = ''
  form.genero = 'Masculino'
  form.dui = ''
  form.email = ''
  form.telefono = ''
  form.fechaNacimiento = ''
  form.direccion = ''
  form.contrasena = ''
  form.idRol = idRolPorNombre('Recepcionista')
  form.estado = 'Activo'
  error.value = ''
}
const openCreate = () => {
  editingUser.value = null
  resetForm()
  modal.value = true
}
const openEdit = (user: Usuario) => {
  editingUser.value = user
  form.nombres = user.nombres
  form.apellidos = user.apellidos
  form.genero = user.genero
  form.dui = user.dui
  form.email = user.email
  form.telefono = user.telefono
  form.fechaNacimiento = user.fechaNacimiento
  form.direccion = user.direccion
  form.contrasena = ''
  form.idRol = idRolPorNombre(user.role)
  form.estado = user.estado
  error.value = ''
  modal.value = true
}
// Un entrenador se edita en su perfil, porque ahi estan su especialidad y tarifa
const editar = (user: Usuario) => {
  if (user.role !== 'Entrenador') return openEdit(user)
  const entrenador = trainers.value.find((trainer) => trainer.nombreUsuario === user.nombreUsuario)
  router.push(entrenador ? `/entrenadores/${entrenador.id}` : '/entrenadores')
}
const closeModal = () => {
  modal.value = false
  editingUser.value = null
}
// El backend usa el DUI como nombre de usuario; contrasena vacia al editar = no cambiarla
const guardar = async () => {
  guardando.value = true
  error.value = ''
  const datos = {
    nombres: form.nombres,
    apellidos: form.apellidos,
    genero: form.genero,
    fechaNacimiento: form.fechaNacimiento,
    direccion: form.direccion,
    email: form.email,
    telefono: form.telefono,
    dui: form.dui,
    contrasena: form.contrasena,
    idRol: form.idRol,
    idEstado: idEstado(form.estado),
  }
  try {
    if (editingUser.value) {
      const actualizado = await api.put<UsuarioApi>(`/usuarios/${editingUser.value.id}`, datos)
      if (esUsuarioConectado.value) guardarSesion(aSessionUser(actualizado))
    } else {
      await api.post('/usuarios', datos)
    }
    await cargarUsuarios()
    closeModal()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'No se pudo guardar el usuario.'
  } finally {
    guardando.value = false
  }
}
</script>
