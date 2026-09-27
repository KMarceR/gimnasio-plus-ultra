import { createRouter, createWebHistory } from 'vue-router'
import { useAuth, type UserRole } from '@/composables/useAuth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  scrollBehavior: () => ({ left: 0, top: 0 }),
  routes: [
    { path: '/login', name: 'Login', component: () => import('@/views/Auth/Login.vue'), meta: { title: 'Iniciar sesión', public: true } },
    { path: '/', name: 'Dashboard', component: () => import('@/views/Dashboard.vue'), meta: { title: 'Dashboard' } },
    { path: '/clientes', name: 'Clientes', component: () => import('@/views/Clientes.vue'), meta: { title: 'Clientes', roles: ['Administrador', 'Recepcionista', 'Entrenador'] as UserRole[] } },
    { path: '/clientes/:id', name: 'ClientePerfil', component: () => import('@/views/ClientePerfil.vue'), meta: { title: 'Perfil de cliente', roles: ['Administrador', 'Recepcionista', 'Entrenador'] as UserRole[] } },
    { path: '/suscripciones', name: 'Suscripciones', component: () => import('@/views/Suscripciones.vue'), meta: { title: 'Suscripciones', roles: ['Administrador', 'Recepcionista'] as UserRole[] } },
    { path: '/qr', name: 'Control QR', component: () => import('@/views/ControlQR.vue'), meta: { title: 'Control QR', roles: ['Administrador', 'Recepcionista'] as UserRole[] } },
    { path: '/recepcion', name: 'Recepción', component: () => import('@/views/Recepcion.vue'), meta: { title: 'Recepción', roles: ['Administrador', 'Recepcionista'] as UserRole[] } },
    { path: '/entrenadores', name: 'Entrenadores', component: () => import('@/views/Entrenadores.vue'), meta: { title: 'Entrenadores', roles: ['Administrador', 'Recepcionista', 'Entrenador'] as UserRole[] } },
    { path: '/entrenadores/:id', name: 'EntrenadorPerfil', component: () => import('@/views/EntrenadorPerfil.vue'), meta: { title: 'Perfil de entrenador', roles: ['Administrador', 'Recepcionista', 'Entrenador'] as UserRole[] } },
    { path: '/sesiones', name: 'Sesiones', component: () => import('@/views/Sesiones.vue'), meta: { title: 'Sesiones', roles: ['Administrador', 'Entrenador'] as UserRole[] } },
    { path: '/usuarios', name: 'Usuarios', component: () => import('@/views/Usuarios.vue'), meta: { title: 'Usuarios', roles: ['Administrador'] as UserRole[] } },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

router.beforeEach((to) => {
  const { currentUser } = useAuth()
  document.title = `${String(to.meta.title || 'Plus Ultra')} | Plus Ultra`
  if (to.meta.public) {
    if (currentUser.value && to.path === '/login') return '/'
    return true
  }
  if (!currentUser.value) return '/login'
  const roles = to.meta.roles as UserRole[] | undefined
  if (roles && !roles.includes(currentUser.value.role)) return '/'
  return true
})

export default router
