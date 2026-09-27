<template>
  <FullScreenLayout>
    <div
      class="relative flex min-h-screen items-center justify-center overflow-hidden bg-gray-50 p-4"
    >
      <div class="pointer-events-none absolute inset-0">
        <div class="absolute left-1/3 top-1/4 h-96 w-96 rounded-full bg-brand-500/5 blur-3xl"></div>
        <div
          class="absolute bottom-1/4 right-1/3 h-80 w-80 rounded-full bg-orange-500/5 blur-3xl"
        ></div>
      </div>

      <div class="relative w-full max-w-md">
        <div class="mb-8 text-center">
          <div
            class="mb-4 inline-flex h-14 w-14 items-center justify-center rounded-2xl bg-brand-500"
          >
            <ZapIcon :size="24" class="text-white" />
          </div>
          <h1 class="font-body text-3xl font-bold tracking-wide text-gray-900">Plus Ultra</h1>
          <p class="mt-1.5 text-sm text-gray-500">Sistema de Gestión de Gimnasio</p>
        </div>

        <div class="space-y-5 rounded-2xl border border-gray-200 bg-white p-7 shadow-theme-xs">
          <div>
            <h2 class="text-lg font-bold text-gray-900">Iniciar sesión</h2>
            <p class="mt-0.5 text-sm text-gray-500">Ingresá tus credenciales para continuar</p>
          </div>

          <form class="space-y-4" @submit.prevent="submit">
            <div>
              <label
                class="mb-1.5 block text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Usuario</label
              >
              <div class="relative">
                <UserIcon
                  :size="15"
                  class="absolute start-3.5 top-1/2 -translate-y-1/2 text-gray-400"
                />
                <input
                  v-model="nombreUsuario"
                  type="text"
                  required
                  placeholder="nombre_usuario"
                  class="w-full rounded-xl border border-gray-200 bg-gray-50 py-2.5 ps-10 pe-4 text-sm text-gray-900 placeholder:text-gray-400 focus:border-brand-400 focus:outline-none"
                />
              </div>
            </div>

            <div>
              <label
                class="mb-1.5 block text-xs font-semibold uppercase tracking-wider text-gray-500"
                >Contraseña</label
              >
              <div class="relative">
                <LockIcon
                  :size="15"
                  class="absolute start-3.5 top-1/2 -translate-y-1/2 text-gray-400"
                />
                <input
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  required
                  placeholder="••••••••"
                  class="w-full rounded-xl border border-gray-200 bg-gray-50 py-2.5 ps-10 pe-10 text-sm text-gray-900 placeholder:text-gray-400 focus:border-brand-400 focus:outline-none"
                />
                <button
                  type="button"
                  class="absolute end-3.5 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                  @click="showPassword = !showPassword"
                >
                  <EyeOffIcon v-if="showPassword" :size="15" />
                  <EyeIcon v-else :size="15" />
                </button>
              </div>
            </div>

            <Alert v-if="error" variant="error" title="No se pudo ingresar" :message="error" />

            <Button type="submit" :disabled="loading" class-name="w-full justify-center">
              <RefreshCwIcon v-if="loading" :size="15" class="animate-spin" />
              <LogInIcon v-else :size="15" />
              {{ loading ? 'Verificando...' : 'Ingresar al sistema' }}
            </Button>
          </form>
        </div>
      </div>
    </div>
  </FullScreenLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  Zap as ZapIcon,
  User as UserIcon,
  Lock as LockIcon,
  Eye as EyeIcon,
  EyeOff as EyeOffIcon,
  LogIn as LogInIcon,
  RefreshCw as RefreshCwIcon,
} from 'lucide-vue-next'
import FullScreenLayout from '@/components/layout/FullScreenLayout.vue'
import Alert from '@/components/ui/Alert.vue'
import Button from '@/components/ui/Button.vue'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const { login } = useAuth()

const nombreUsuario = ref('')
const password = ref('')
const showPassword = ref(false)
const loading = ref(false)
const error = ref('')

const submit = () => {
  loading.value = true
  error.value = ''
  setTimeout(() => {
    const ok = login(nombreUsuario.value, password.value)
    if (ok) {
      router.push('/')
    } else {
      error.value = 'Credenciales incorrectas. Revisá el usuario y contraseña.'
      loading.value = false
    }
  }, 600)
}
</script>
