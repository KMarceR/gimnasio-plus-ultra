import { computed, ref } from 'vue'
import type { Genero } from './useGymData'
import { api } from '@/services/api'

export type UserRole = 'Administrador' | 'Gerente' | 'Recepcionista' | 'Entrenador'

// `usuarios` extiende `persona` (FK id_persona), por eso el usuario de sesión
// lleva los mismos campos de persona.
export interface SessionUser {
  id: string
  nombreUsuario: string
  nombres: string
  apellidos: string
  genero: Genero
  fechaNacimiento: string
  direccion: string
  email: string
  telefono: string
  dui: string
  role: UserRole
  initials: string
}

export interface UsuarioApi {
  idUsuario: number
  nombreUsuario: string
  nombreRol: string
  nombreEstado: string
  nombres: string
  apellidos: string
  genero: Genero
  fechaNacimiento: string
  direccion: string
  email: string
  telefono: string
  dui: string
}

export const aSessionUser = (usuario: UsuarioApi): SessionUser => ({
  id: String(usuario.idUsuario),
  nombreUsuario: usuario.nombreUsuario,
  nombres: usuario.nombres,
  apellidos: usuario.apellidos,
  genero: usuario.genero,
  fechaNacimiento: usuario.fechaNacimiento,
  direccion: usuario.direccion,
  email: usuario.email,
  telefono: usuario.telefono,
  dui: usuario.dui,
  role: usuario.nombreRol as UserRole,
  initials: `${usuario.nombres.charAt(0)}${usuario.apellidos.charAt(0)}`.toUpperCase(),
})

const storedUser = localStorage.getItem('plus-ultra-user')
const currentUser = ref<SessionUser | null>(storedUser ? JSON.parse(storedUser) : null)

export function useAuth() {
  const isAuthenticated = computed(() => currentUser.value !== null)

  // Lanza ApiError si el usuario o la contrasena no son validos
  const login = async (nombreUsuario: string, contrasena: string) => {
    const usuario = await api.post<UsuarioApi>('/auth/login', { nombreUsuario, contrasena })
    guardarSesion(aSessionUser(usuario))
  }

  // Tambien se usa cuando el usuario conectado edita sus propios datos
  const guardarSesion = (usuario: SessionUser) => {
    currentUser.value = usuario
    localStorage.setItem('plus-ultra-user', JSON.stringify(usuario))
  }

  const logout = () => {
    currentUser.value = null
    localStorage.removeItem('plus-ultra-user')
  }

  return { currentUser, isAuthenticated, login, logout, guardarSesion }
}
