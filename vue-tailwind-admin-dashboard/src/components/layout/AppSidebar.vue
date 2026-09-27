<template>
  <aside
    class="fixed start-0 top-0 z-999 flex h-screen w-60 shrink-0 flex-col border-e border-gray-200 bg-white"
  >
    <div class="border-b border-gray-200 px-5 py-5">
      <router-link to="/" class="flex items-center gap-3">
        <span class="flex h-9 w-9 shrink-0 items-center justify-center rounded-xl bg-brand-500">
          <ZapIcon :size="17" class="text-white" />
        </span>
        <span>
          <strong
            class="font-body block text-sm font-bold uppercase leading-none tracking-widest text-gray-900"
            >Plus Ultra</strong
          >
          <small class="mt-0.5 block text-xs text-gray-500">Gestión Integral</small>
        </span>
      </router-link>
    </div>

    <nav class="flex-1 space-y-0.5 overflow-y-auto p-3">
      <router-link
        v-for="item in navItems"
        :key="item.path"
        :to="item.path"
        class="menu-item group"
        :class="isActive(item.path) ? 'menu-item-active' : 'menu-item-inactive'"
      >
        <span :class="isActive(item.path) ? 'menu-item-icon-active' : 'menu-item-icon-inactive'">
          <component :is="item.icon" :size="16" />
        </span>
        <span class="menu-item-text flex-1 text-start">{{ item.name }}</span>
        <ChevronRightIcon v-if="isActive(item.path)" :size="13" class="opacity-60" />
      </router-link>
    </nav>

    <div class="space-y-3 border-t border-gray-200 p-4">
      <div class="flex items-center gap-3 px-1">
        <div
          class="font-body flex h-8 w-8 shrink-0 items-center justify-center rounded-full text-xs font-bold"
          :class="roleAvatarColor[currentUser?.role ?? 'Recepcionista']"
        >
          {{ currentUser?.initials }}
        </div>
        <div class="min-w-0 flex-1">
          <p class="truncate text-xs font-semibold text-gray-800">
            {{ currentUser?.nombres }} {{ currentUser?.apellidos }}
          </p>
          <p class="truncate text-xs text-gray-500">{{ currentUser?.role }}</p>
        </div>
      </div>
      <button
        type="button"
        title="Cerrar sesión"
        class="flex w-full items-center justify-center gap-1.5 rounded-xl border border-transparent px-3 py-2 text-xs text-gray-500 transition-colors hover:border-error-100 hover:bg-error-50 hover:text-error-600"
        @click="handleLogout"
      >
        <LogOutIcon :size="14" />
        Cerrar sesión
      </button>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  LayoutDashboard as LayoutDashboardIcon,
  Users as UsersIcon,
  CreditCard as CreditCardIcon,
  Dumbbell as DumbbellIcon,
  QrCode as QrCodeIcon,
  Shield as ShieldIcon,
  Calendar as CalendarIcon,
  Settings as SettingsIcon,
  ChevronRight as ChevronRightIcon,
  LogOut as LogOutIcon,
  Zap as ZapIcon,
} from 'lucide-vue-next'
import { useAuth, type UserRole } from '@/composables/useAuth'

const route = useRoute()
const router = useRouter()
const { currentUser, logout } = useAuth()

interface NavItem {
  name: string
  path: string
  icon: typeof LayoutDashboardIcon
  roles?: UserRole[]
}

const allNavItems: NavItem[] = [
  { name: 'Dashboard', path: '/', icon: LayoutDashboardIcon },
  { name: 'Clientes', path: '/clientes', icon: UsersIcon },
  {
    name: 'Suscripciones',
    path: '/suscripciones',
    icon: CreditCardIcon,
    roles: ['Administrador', 'Gerente', 'Recepcionista'],
  },
  {
    name: 'Control QR',
    path: '/qr',
    icon: QrCodeIcon,
    roles: ['Administrador', 'Gerente', 'Recepcionista'],
  },
  {
    name: 'Recepción',
    path: '/recepcion',
    icon: ShieldIcon,
    roles: ['Administrador', 'Gerente', 'Recepcionista'],
  },
  { name: 'Entrenadores', path: '/entrenadores', icon: DumbbellIcon },
  {
    name: 'Sesiones',
    path: '/sesiones',
    icon: CalendarIcon,
    roles: ['Administrador', 'Gerente', 'Entrenador'],
  },
  { name: 'Usuarios', path: '/usuarios', icon: SettingsIcon, roles: ['Administrador'] },
]

const navItems = computed(() =>
  allNavItems.filter(
    (item) => !item.roles || item.roles.includes(currentUser.value?.role as UserRole),
  ),
)

const roleAvatarColor: Record<UserRole, string> = {
  Administrador: 'bg-orange-100 text-orange-600',
  Gerente: 'bg-purple-100 text-purple-600',
  Recepcionista: 'bg-blue-light-100 text-blue-light-600',
  Entrenador: 'bg-brand-100 text-brand-600',
}

const isActive = (path: string) => route.path === path

const handleLogout = () => {
  logout()
  router.push('/login')
}
</script>
