import { computed, ref } from 'vue'

export type UserRole = 'Administrador' | 'Recepcionista' | 'Entrenador'

export interface SessionUser {
  id: string
  name: string
  email: string
  role: UserRole
  initials: string
}

const demoUsers: Array<SessionUser & { password: string }> = [
  { id: 'u1', name: 'Carlos Mendoza', email: 'admin@plusultra.com', password: 'admin123', role: 'Administrador', initials: 'CM' },
  { id: 'u2', name: 'Laura Gómez', email: 'recepcion@plusultra.com', password: 'recepcion123', role: 'Recepcionista', initials: 'LG' },
  { id: 'u3', name: 'Diego Ramírez', email: 'entrenador@plusultra.com', password: 'entrenador123', role: 'Entrenador', initials: 'DR' },
]

const storedUser = localStorage.getItem('plus-ultra-user')
const currentUser = ref<SessionUser | null>(storedUser ? JSON.parse(storedUser) : null)

export function useAuth() {
  const isAuthenticated = computed(() => currentUser.value !== null)

  const login = (email: string, password: string) => {
    const user = demoUsers.find((item) => item.email === email && item.password === password)
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
    return user ? { email: user.email, password: user.password } : { email: '', password: '' }
  }

  return { currentUser, isAuthenticated, login, logout, fillDemo, demoUsers }
}
