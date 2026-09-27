import { computed, ref } from 'vue'
import type { Genero } from './useGymData'

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

const demoUsers: Array<SessionUser & { password: string }> = [
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
    password: 'plusultra123',
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
    password: 'plusultra123',
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
    password: 'plusultra123',
    role: 'Entrenador',
    initials: 'NP',
  },
]

const storedUser = localStorage.getItem('plus-ultra-user')
const currentUser = ref<SessionUser | null>(storedUser ? JSON.parse(storedUser) : null)

export function useAuth() {
  const isAuthenticated = computed(() => currentUser.value !== null)

  const login = (nombreUsuario: string, password: string) => {
    const user = demoUsers.find((item) => item.nombreUsuario === nombreUsuario && item.password === password)
    if (!user) return false
    const { password: _password, ...sessionUser } = user
    currentUser.value = sessionUser
    localStorage.setItem('plus-ultra-user', JSON.stringify(sessionUser))
    return true
  }

  const logout = () => {
    currentUser.value = null
    localStorage.removeItem('plus-ultra-user')
  }

  const fillDemo = (role: UserRole) => {
    const user = demoUsers.find((item) => item.role === role)
    return user ? { nombreUsuario: user.nombreUsuario, password: user.password } : { nombreUsuario: '', password: '' }
  }

  return { currentUser, isAuthenticated, login, logout, fillDemo, demoUsers }
}
