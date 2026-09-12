<template>
  <AdminLayout
    ><div class="space-y-6">
      <header class="flex items-end justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Accesos del sistema</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Usuarios</h1>
        </div>
        <button class="rounded-lg bg-brand-500 px-4 py-2.5 text-sm font-medium text-white">
          + Nuevo Usuario
        </button>
      </header>
      <section class="rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6">
        <div class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
              <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
                <th class="py-3 text-start">Usuario</th>
                <th class="py-3 text-start">Rol</th>
                <th class="py-3 text-start">Estado</th>
                <th class="py-3 text-start">Último acceso</th>
                <th class="py-3 text-end">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.email" class="border-b border-gray-100">
                <td class="py-4">
                  <div class="flex items-center gap-3">
                    <span
                      class="flex h-10 w-10 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
                      >{{ user.initials }}</span
                    >
                    <div>
                      <p class="text-sm font-semibold text-gray-800">{{ user.name }}</p>
                      <p class="text-xs text-gray-500">{{ user.email }}</p>
                    </div>
                  </div>
                </td>
                <td class="py-4">
                  <span
                    :class="roleClass(user.role)"
                    class="rounded-full px-2.5 py-1 text-xs font-medium"
                    >{{ user.role }}</span
                  >
                </td>
                <td class="py-4">
                  <button
                    :class="user.active ? 'bg-success-500' : 'bg-gray-300'"
                    class="relative h-5 w-9 rounded-full"
                  >
                    <span
                      class="absolute top-0.5 h-4 w-4 rounded-full bg-white transition"
                      :class="user.active ? 'inset-e-0.5' : 'inset-s-0.5'"
                    ></span>
                  </button>
                </td>
                <td class="py-4 text-sm text-gray-600">Hoy, 08:12</td>
                <td class="py-4 text-end">
                  <button class="text-sm font-medium text-brand-600">Editar</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </div></AdminLayout
  >
</template>
<script setup lang="ts">
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useAuth, type UserRole } from '@/composables/useAuth'
const { demoUsers } = useAuth()
const users = demoUsers.map(({ password: _password, ...user }) => ({ ...user, active: true }))
const roleClass = (role: UserRole) =>
  ({
    Administrador: 'bg-orange-50 text-orange-600',
    Recepcionista: 'bg-blue-light-50 text-blue-light-600',
    Entrenador: 'bg-success-50 text-success-600',
  })[role]
</script>
