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
          <Button class-name="mt-6 w-full justify-center" :disabled="scanning" @click="scan">
            {{ scanning ? 'Escaneando...' : 'Escanear código QR' }}
          </Button>
          <Alert
            v-if="result"
            class="mt-5 text-start"
            :variant="result.ok ? 'success' : 'error'"
            :title="result.ok ? 'Acceso registrado' : 'Cliente inactivo'"
            :message="`${result.nombre} · ${result.hora}`"
          />
        </section>
        <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
          <h2 class="text-lg font-semibold text-gray-900">Log de accesos del día</h2>
          <div class="mt-5 divide-y divide-gray-100">
            <div
              v-for="record in asistencia"
              :key="record.id"
              class="flex items-center gap-3 py-4 first:pt-0"
            >
              <span
                class="flex h-10 w-10 items-center justify-center rounded-full bg-brand-50 text-xs font-bold text-brand-600"
                >↓</span
              >
              <div class="flex-1">
                <p class="text-sm font-semibold text-gray-800">
                  {{
                    memberById(record.clienteId)
                      ? nombreCompleto(memberById(record.clienteId)!)
                      : 'Cliente eliminado'
                  }}
                </p>
                <p class="text-xs text-gray-500">{{ record.fecha }} · {{ record.hora }}</p>
              </div>
              <span
                :class="
                  record.estado === 'Activo' || record.estado === 'Completada'
                    ? 'text-brand-600'
                    : 'text-error-600'
                "
                class="text-sm font-semibold"
                >{{ record.estado }}</span
              >
            </div>
            <p v-if="!asistencia.length" class="py-6 text-center text-sm text-gray-500">
              Sin registros hoy.
            </p>
          </div>
        </section>
      </div>
    </div></AdminLayout
  >
</template>
<script setup lang="ts">
import { ref } from 'vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Button from '@/components/ui/Button.vue'
import Alert from '@/components/ui/Alert.vue'
import { useGymData, nombreCompleto } from '@/composables/useGymData'
const { members, asistencia, memberById, registerAccess } = useGymData()
const scanning = ref(false)
const result = ref<{ nombre: string; hora: string; ok: boolean } | null>(null)
const scan = () => {
  scanning.value = true
  window.setTimeout(() => {
    const member = members.value[Math.floor(Math.random() * members.value.length)]
    registerAccess(member)
    result.value = {
      nombre: nombreCompleto(member),
      hora: new Date().toLocaleTimeString('es-SV', { hour: '2-digit', minute: '2-digit' }),
      ok: member.estado === 'Activo',
    }
    scanning.value = false
  }, 450)
}
</script>
