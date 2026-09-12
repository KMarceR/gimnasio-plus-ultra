<template>
  <AdminLayout
    ><div class="space-y-6">
      <header>
        <p class="text-sm font-medium text-brand-600">Registro de accesos</p>
        <h1 class="mt-1 text-3xl font-semibold text-gray-900">Control QR</h1>
      </header>
      <div class="grid grid-cols-1 gap-6 lg:grid-cols-[0.8fr_1.2fr]">
        <section
          class="rounded-2xl border border-gray-200 bg-white p-6 text-center shadow-theme-xs"
        >
          <div
            class="mx-auto flex aspect-square max-w-70 items-center justify-center rounded-2xl border-4 border-dashed border-brand-300 bg-brand-50 p-8"
          >
            <div class="grid grid-cols-5 gap-2 opacity-80">
              <span
                v-for="n in 25"
                :key="n"
                :class="n % 3 === 0 ? 'bg-brand-500' : 'bg-gray-900'"
                class="h-6 w-6 rounded-sm"
              ></span>
            </div>
          </div>
          <div class="mx-auto mt-5 h-1 max-w-70 animate-pulse rounded-full bg-brand-500"></div>
          <button
            class="mt-6 w-full rounded-xl bg-brand-500 py-3 font-medium text-white hover:bg-brand-600"
            @click="scan"
          >
            {{ scanning ? 'Escaneando...' : 'Escanear código QR' }}
          </button>
          <div
            v-if="result"
            class="mt-5 rounded-xl p-4 text-start"
            :class="result.status === 'OK' ? 'bg-success-50' : 'bg-error-50'"
          >
            <p
              class="text-sm font-semibold"
              :class="result.status === 'OK' ? 'text-success-700' : 'text-error-700'"
            >
              {{ result.status }} · {{ result.type }}
            </p>
            <p class="mt-1 font-medium text-gray-800">{{ result.member }}</p>
            <p class="text-xs text-gray-500">{{ result.time }}</p>
          </div>
        </section>
        <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
          <h2 class="text-lg font-semibold text-gray-900">Log de accesos del día</h2>
          <div class="mt-5 divide-y divide-gray-100">
            <div
              v-for="access in accessLog"
              :key="access.id"
              class="flex items-center gap-3 py-4 first:pt-0"
            >
              <span
                :class="
                  access.type === 'Entrada'
                    ? 'bg-success-50 text-success-600'
                    : 'bg-blue-light-50 text-blue-light-600'
                "
                class="flex h-10 w-10 items-center justify-center rounded-full text-xs font-bold"
                >{{ access.type === 'Entrada' ? '↓' : '↑' }}</span
              >
              <div class="flex-1">
                <p class="text-sm font-semibold text-gray-800">{{ access.member }}</p>
                <p class="text-xs text-gray-500">{{ access.type }} · {{ access.time }}</p>
              </div>
              <span
                :class="access.status === 'OK' ? 'text-success-600' : 'text-error-600'"
                class="text-sm font-semibold"
                >{{ access.status }}</span
              >
            </div>
          </div>
        </section>
      </div>
    </div></AdminLayout
  >
</template>
<script setup lang="ts">
import { ref } from 'vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import { useGymData } from '@/composables/useGymData'
const { members, accessLog, registerAccess } = useGymData()
const scanning = ref(false)
const result = ref<{ member: string; type: string; time: string; status: string } | null>(null)
const scan = () => {
  scanning.value = true
  window.setTimeout(() => {
    const member = members.value[Math.floor(Math.random() * members.value.length)]
    const type = Math.random() > 0.5 ? 'Entrada' : 'Salida'
    registerAccess(member, type)
    result.value = {
      member: member.name,
      type,
      time: new Date().toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' }),
      status: member.status === 'Vencido' ? 'Error' : 'OK',
    }
    scanning.value = false
  }, 450)
}
</script>
