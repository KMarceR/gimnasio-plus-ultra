<template>
  <FullScreenLayout>
    <main class="flex min-h-screen items-center justify-center bg-gray-50 p-5">
      <section class="grid w-full max-w-5xl overflow-hidden rounded-3xl border border-gray-200 bg-white shadow-theme-lg lg:grid-cols-[1.05fr_0.95fr]">
        <div class="hidden bg-brand-500 p-12 text-white lg:flex lg:flex-col lg:justify-between">
          <div>
            <div class="flex items-center gap-3"><span class="flex h-12 w-12 items-center justify-center rounded-2xl bg-white text-2xl font-bold text-brand-600">P</span><strong class="text-xl tracking-[0.18em]">PLUS ULTRA</strong></div>
            <h1 class="mt-20 max-w-sm text-5xl font-semibold leading-tight">Tu gimnasio, bajo control.</h1>
            <p class="mt-5 max-w-sm text-brand-50">Administra socios, sesiones, accesos y entrenadores desde un solo lugar.</p>
          </div>
          <p class="text-sm text-brand-100">Gestión Integral · 2026</p>
        </div>
        <div class="p-7 sm:p-12">
          <div class="mb-8 lg:hidden"><span class="flex h-11 w-11 items-center justify-center rounded-xl bg-brand-500 text-xl font-bold text-white">P</span><p class="mt-3 text-lg font-bold tracking-[0.16em] text-gray-900">PLUS ULTRA</p></div>
          <p class="text-sm font-medium text-brand-600">Bienvenido de nuevo</p>
          <h2 class="mt-2 text-3xl font-semibold text-gray-900">Inicia sesión</h2>
          <p class="mt-2 text-sm text-gray-500">Usa una cuenta demo para explorar el sistema.</p>
          <form class="mt-8 space-y-5" @submit.prevent="submit">
            <div><label class="mb-2 block text-sm font-medium text-gray-700">Email</label><input v-model="email" type="email" class="h-12 w-full rounded-xl border border-gray-200 px-4 text-sm outline-none focus:border-brand-500 focus:ring-4 focus:ring-brand-500/10" placeholder="admin@plusultra.com" /></div>
            <div><label class="mb-2 block text-sm font-medium text-gray-700">Contraseña</label><div class="relative"><input v-model="password" :type="showPassword ? 'text' : 'password'" class="h-12 w-full rounded-xl border border-gray-200 px-4 pe-12 text-sm outline-none focus:border-brand-500 focus:ring-4 focus:ring-brand-500/10" placeholder="••••••••" /><button type="button" class="absolute inset-y-0 inset-e-4 text-xs font-medium text-gray-500" @click="showPassword = !showPassword">{{ showPassword ? 'Ocultar' : 'Mostrar' }}</button></div></div>
            <p v-if="error" class="rounded-lg bg-error-50 px-3 py-2 text-sm text-error-600">{{ error }}</p>
            <button class="h-12 w-full rounded-xl bg-brand-500 font-medium text-white transition hover:bg-brand-600">Iniciar Sesión</button>
          </form>
          <div class="mt-8"><p class="text-xs font-semibold uppercase tracking-wider text-gray-400">Acceso rápido</p><div class="mt-3 grid gap-2 sm:grid-cols-3"><button v-for="demo in demos" :key="demo.role" type="button" class="rounded-xl border border-gray-200 p-3 text-start transition hover:border-brand-300 hover:bg-brand-50" @click="selectDemo(demo.role)"><span class="block text-sm font-semibold text-gray-800">{{ demo.role }}</span><span class="mt-1 block text-xs text-gray-500">{{ demo.email }}</span></button></div></div>
        </div>
      </section>
    </main>
  </FullScreenLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import FullScreenLayout from '@/components/layout/FullScreenLayout.vue'
import { useAuth, type UserRole } from '@/composables/useAuth'

const router = useRouter()
const { login, fillDemo } = useAuth()
const email = ref('')
const password = ref('')
const showPassword = ref(false)
const error = ref('')
const demos: Array<{ role: UserRole; email: string }> = [
  { role: 'Administrador', email: 'admin@plusultra.com' },
  { role: 'Recepcionista', email: 'recepcion@plusultra.com' },
  { role: 'Entrenador', email: 'entrenador@plusultra.com' },
]
const selectDemo = (role: UserRole) => {
  const demo = fillDemo(role)
  email.value = demo.email
  password.value = demo.password
}
const submit = () => { error.value = login(email.value, password.value) ? '' : 'El email o la contraseña no son correctos.'; if (!error.value) router.push('/') }
</script>
