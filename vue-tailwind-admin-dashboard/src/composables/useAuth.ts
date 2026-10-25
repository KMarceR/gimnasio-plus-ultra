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

interface UsuarioResponse {
  idUsuario: number
  nombreUsuario: string
  nombreRol: string
  nombres: string
  apellidos: string
  genero: Genero
  fechaNacimiento: string
  direccion: string
  email: string
  telefono: string
  dui: string
}

// Temporal: la pantalla Usuarios los muestra hasta conectarla con la API
const demoUsers: SessionUser[] = [
  {
    id: '1',
    nombreUsuario: 'daniel_admin',
    nombres: 'Daniel Enrique',
    apellidos: 'Guevara Gómez',
    genero: 'Masculino',
    fechaNacimiento: '1985-06-15',
    direccion: 'Colonia San Benito, San Salvador',
    email: 'daniel.guevara@plusultra.com',
    telefono: '2257-8888',
    dui: '01234567-9',
    role: 'Administrador',
    initials: 'DG',
  },
  {
    id: '2',
    nombreUsuario: 'karla_gerente',
    nombres: 'Karla Marcela',
    apellidos: 'Bonilla Rosales',
    genero: 'Femenino',
    fechaNacimiento: '2002-11-30',
    direccion: 'Residencial San Francisco, San Salvador',
    email: 'karla.bonilla@plusultra.com',
    telefono: '7512-3456',
    dui: '06010191-2',
    role: 'Gerente',
    initials: 'KB',
  },
  {
    id: '3',
    nombreUsuario: 'nelson_coach',
    nombres: 'Nelson Eduardo',
    apellidos: 'Palacios Henríquez',
    genero: 'Masculino',
    fechaNacimiento: '1991-02-03',
    direccion: 'Urbanización Prados de Venecia, Soyapango',
    email: 'nelson.trainer@gmail.com',
    telefono: '7745-9988',
    dui: '04891234-5',
    role: 'Entrenador',
    initials: 'NP',
  },
]

const storedUser = localStorage.getItem('plus-ultra-user')
const currentUser = ref<SessionUser | null>(storedUser ? JSON.parse(storedUser) : null)

export function useAuth() {
  const isAuthenticated = computed(() => currentUser.value !== null)

  // Lanza ApiError si el usuario o la contrasena no son validos
  const login = async (nombreUsuario: string, contrasena: string) => {
    const usuario = await api.post<UsuarioResponse>('/auth/login', { nombreUsuario, contrasena })
    const sessionUser: SessionUser = {
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
    }
    currentUser.value = sessionUser
    localStorage.setItem('plus-ultra-user', JSON.stringify(sessionUser))
  }

  const logout = () => {
    currentUser.value = null
    localStorage.removeItem('plus-ultra-user')
  }

  return { currentUser, isAuthenticated, login, logout, demoUsers }
}
