<template>
  <AdminLayout
    ><div v-if="trainer" class="space-y-6">
      <button
        class="flex items-center gap-2 text-sm font-medium text-gray-600"
        @click="router.push('/entrenadores')"
      >
        <span class="flex h-9 w-9 items-center justify-center rounded-full border border-gray-200"
          >←</span
        >
        Volver a entrenadores
      </button>
      <section
        class="flex flex-col gap-4 rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs sm:flex-row sm:items-center"
      >
        <span
          class="flex h-20 w-20 items-center justify-center rounded-2xl bg-brand-50 text-2xl font-semibold text-brand-600"
          >{{ iniciales(trainer) }}</span
        >
        <div class="flex-1">
          <p class="text-sm text-gray-500">Perfil profesional</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">{{ nombreCompleto(trainer) }}</h1>
          <p class="mt-1 text-brand-600">{{ trainer.especialidad }}</p>
        </div>
        <Badge :color="trainer.estado === 'Activo' ? 'primary' : 'light'">{{
          trainer.estado
        }}</Badge>
      </section>
      <div class="grid grid-cols-1 gap-6 lg:grid-cols-3">
        <section class="rounded-2xl border border-gray-200 bg-white p-6 lg:col-span-2">
          <h2 class="text-lg font-semibold text-gray-900">Información del entrenador</h2>
          <dl class="mt-5 grid gap-5 sm:grid-cols-2">
            <div>
              <dt class="text-xs text-gray-500">DUI</dt>
              <dd class="mt-1 font-mono text-sm font-medium text-gray-800">{{ trainer.dui }}</dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Género</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">{{ trainer.genero }}</dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Email</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">{{ trainer.email }}</dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Teléfono</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">{{ trainer.telefono }}</dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Fecha de nacimiento</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">{{ trainer.fechaNacimiento }}</dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Tarifa por sesión</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">${{ trainer.tarifaPorSesion }}</dd>
            </div>
            <div class="sm:col-span-2">
              <dt class="text-xs text-gray-500">Dirección</dt>
              <dd class="mt-1 text-sm font-medium text-gray-800">
                {{ trainer.direccion || 'No registrada' }}
              </dd>
            </div>
            <div>
              <dt class="text-xs text-gray-500">Usuario</dt>
              <dd class="mt-1 font-mono text-sm font-medium text-gray-800">
                {{ trainer.nombreUsuario }}
              </dd>
            </div>
          </dl>
        </section>
        <section class="rounded-2xl border border-gray-200 bg-white p-6">
          <h2 class="text-lg font-semibold text-gray-900">Sesiones</h2>
          <div class="mt-5 space-y-4">
            <div class="rounded-xl bg-gray-50 p-4">
              <p class="text-xs text-gray-500">Sesiones totales</p>
              <p class="mt-1 text-2xl font-semibold text-gray-900">{{ misSesiones.length }}</p>
            </div>
            <div class="rounded-xl bg-gray-50 p-4">
              <p class="text-xs text-gray-500">Completadas</p>
              <p class="mt-1 text-2xl font-semibold text-gray-900">{{ completadas.length }}</p>
            </div>
            <div class="rounded-xl bg-gray-50 p-4">
              <p class="text-xs text-gray-500">Ingresos generados</p>
              <p class="mt-1 text-2xl font-semibold text-gray-900">
                ${{ (completadas.length * trainer.tarifaPorSesion).toLocaleString('es-SV') }}
              </p>
            </div>
          </div>
        </section>
      </div>
      <section class="rounded-2xl border border-gray-200 bg-white p-6 shadow-theme-xs">
        <h2 class="text-lg font-semibold text-gray-900">Historial de sesiones</h2>
        <div class="mt-5 divide-y divide-gray-100">
          <div
            v-for="session in misSesiones"
            :key="session.id"
            class="flex items-center justify-between py-3"
          >
            <div>
              <p class="text-sm font-semibold text-gray-800">
                {{
                  memberById(session.clienteId)
                    ? nombreCompleto(memberById(session.clienteId)!)
                    : 'Cliente eliminado'
                }}
              </p>
              <p class="text-xs text-gray-500">
                {{ session.fecha }} · {{ session.horaInicio }} - {{ session.horaFin }}
              </p>
            </div>
            <Badge :color="sessionColor(session.estado)">{{ session.estado }}</Badge>
          </div>
        </div>
        <p v-if="!misSesiones.length" class="py-6 text-center text-sm text-gray-500">
          Este entrenador no tiene sesiones registradas.
        </p>
      </section>
    </div>
    <div v-else class="p-8 text-center text-gray-500">Entrenador no encontrado.</div></AdminLayout
  >
</template>
<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Badge from '@/components/ui/Badge.vue'
import { useGymData, nombreCompleto, iniciales, type SessionStatus } from '@/composables/useGymData'
const route = useRoute()
const router = useRouter()
const { trainerById, memberById, sessions } = useGymData()
const trainer = trainerById(String(route.params.id))
const misSesiones = computed(() =>
  sessions.value.filter((session) => session.entrenadorId === trainer?.id),
)
const completadas = computed(() =>
  misSesiones.value.filter((session) => session.estado === 'Completada'),
)
const sessionColor = (estado: SessionStatus) =>
  ({ Programada: 'info', Confirmada: 'primary', Completada: 'success', Cancelada: 'light' })[
    estado
  ] as 'info' | 'primary' | 'success' | 'light'
</script>
