<template>
  <AdminLayout
    ><div class="space-y-6">
      <header class="flex items-end justify-between">
        <div>
          <p class="text-sm font-medium text-brand-600">Equipo operativo</p>
          <h1 class="mt-1 text-3xl font-semibold text-gray-900">Entrenadores</h1>
        </div>
        <Button v-if="canCreate" size="sm" @click="openCreate">+ Nuevo Entrenador</Button>
      </header>
      <section
        class="overflow-x-auto rounded-2xl border border-gray-200 bg-white p-5 shadow-theme-xs sm:p-6"
      >
        <table class="min-w-full rounded-2xl">
          <thead>
            <tr class="border-b border-gray-100 text-xs uppercase tracking-wider text-gray-400">
              <th class="py-3 text-start">Entrenador</th>
              <th class="py-3 text-start">Especialidad</th>
              <th class="py-3 text-start">Tarifa por sesión</th>
              <th class="py-3 text-start">Usuario</th>
              <th class="py-3 text-start">Estado</th>
              <th class="py-3 text-end">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="trainer in trainers"
              :key="trainer.id"
              class="border-b border-gray-100 transition hover:bg-gray-50"
            >
              <td class="py-4">
                <div class="flex items-center gap-3">
                  <span
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-brand-50 text-xs font-semibold text-brand-600"
                    >{{ iniciales(trainer) }}</span
                  >
                  <p class="text-sm font-semibold text-gray-800">{{ nombreCompleto(trainer) }}</p>
                </div>
              </td>
              <td class="py-4 text-sm text-gray-600">{{ trainer.especialidad }}</td>
              <td class="py-4 text-sm font-semibold text-gray-800">
                ${{ trainer.tarifaPorSesion }}
              </td>
              <td class="py-4 font-mono text-sm text-gray-500">{{ trainer.nombreUsuario }}</td>
              <td class="py-4">
                <Badge :color="trainer.estado === 'Activo' ? 'primary' : 'light'">{{
                  trainer.estado
                }}</Badge>
              </td>
              <td class="py-4 text-end">
                <RouterLink
                  :to="`/entrenadores/${trainer.id}`"
                  class="rounded-lg px-3 py-2 text-sm font-medium text-brand-600 transition hover:bg-brand-50 hover:text-brand-700"
                  >Ver perfil</RouterLink
                >
              </td>
            </tr>
          </tbody>
        </table>
        <p v-if="!trainers.length" class="p-8 text-center text-sm text-gray-500">
          No hay entrenadores registrados.
        </p>
      </section>
    </div>
    <FormularioEntrenador v-if="modal" @close="modal = false" @guardado="modal = false" />
  </AdminLayout>
</template>
<script setup lang="ts">
import { computed, ref } from 'vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import Badge from '@/components/ui/Badge.vue'
import Button from '@/components/ui/Button.vue'
import FormularioEntrenador from '@/components/entrenadores/FormularioEntrenador.vue'
import { useAuth } from '@/composables/useAuth'
import { useGymData, nombreCompleto, iniciales } from '@/composables/useGymData'

const { currentUser } = useAuth()
const { trainers } = useGymData()
const modal = ref(false)
const canCreate = computed(
  () => currentUser.value?.role === 'Administrador' || currentUser.value?.role === 'Gerente',
)
const openCreate = () => {
  modal.value = true
}
</script>
