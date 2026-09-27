import { computed, ref } from 'vue'

export type EstadoGeneral = 'Activo' | 'Inactivo'
export type SessionStatus = 'Programada' | 'Confirmada' | 'Completada' | 'Cancelada'
export type Genero = 'Masculino' | 'Femenino'

// Datos de persona (tabla `persona`)
export interface Member {
  id: string
  nombres: string
  apellidos: string
  genero: Genero
  fechaNacimiento: string
  direccion: string
  email: string
  telefono: string
  dui: string
  planId: string | null
  estado: EstadoGeneral
  notas: string
  creadoEn: string
}

export interface GymPlan {
  id: string
  nombre: string
  detalles: string
  precio: number
  estado: EstadoGeneral
}

export interface Trainer {
  id: string
  nombres: string
  apellidos: string
  genero: Genero
  fechaNacimiento: string
  direccion: string
  email: string
  telefono: string
  dui: string
  nombreUsuario: string
  especialidad: string
  tarifaPorSesion: number
  estado: EstadoGeneral
}

export interface GymSession {
  id: string
  clienteId: string
  entrenadorId: string
  fecha: string
  horaInicio: string
  horaFin: string
  estado: SessionStatus
}

export interface AsistenciaRecord {
  id: string
  clienteId: string
  estado: string
  fecha: string
  hora: string
}

export const nombreCompleto = (persona: { nombres: string; apellidos: string }) =>
  `${persona.nombres} ${persona.apellidos}`

export const iniciales = (persona: { nombres: string; apellidos: string }) =>
  `${persona.nombres.charAt(0)}${persona.apellidos.charAt(0)}`.toUpperCase()

const initialMembers: Member[] = [
  {
    id: '1',
    nombres: 'Roberto Antonio',
    apellidos: 'Zelaya Flores',
    genero: 'Masculino',
    fechaNacimiento: '1998-07-19',
    direccion: 'Colonia Utila, Santa Tecla, La Libertad',
    email: 'roberto.zelaya@outlook.com',
    telefono: '6102-3344',
    dui: '02564718-9',
    planId: '1',
    estado: 'Activo',
    notas: 'Lesión crónica de meniscos en rodilla izquierda. Evitar prensa inclinada pesada.',
    creadoEn: '2026-09-26',
  },
  {
    id: '2',
    nombres: 'Sofía Alejandra',
    apellidos: 'Quintanilla Cruz',
    genero: 'Femenino',
    fechaNacimiento: '2001-04-11',
    direccion: 'Residencial Villa Lourdes, Colón, La Libertad',
    email: 'sofia.quinta@yahoo.com',
    telefono: '7988-1122',
    dui: '05987412-3',
    planId: '2',
    estado: 'Activo',
    notas: 'Meta: Resistencia aeróbica y pérdida de grasa. Preparación para media maratón.',
    creadoEn: '2026-09-26',
  },
]

const initialTrainers: Trainer[] = [
  {
    id: '1',
    nombres: 'Nelson Eduardo',
    apellidos: 'Palacios Henríquez',
    genero: 'Masculino',
    fechaNacimiento: '1991-02-03',
    direccion: 'Urbanización Prados de Venecia, Soyapango',
    email: 'nelson.trainer@gmail.com',
    telefono: '7745-9988',
    dui: '04891234-5',
    nombreUsuario: 'nelson_coach',
    especialidad: 'Culturismo, HIIT y Nutrición Funcional',
    tarifaPorSesion: 15,
    estado: 'Activo',
  },
]

const initialPlans: GymPlan[] = [
  {
    id: '1',
    nombre: 'Membresía Plus Ultra Black',
    detalles: 'Acceso completo ilimitado a zona de pesas, cardio, spinning y evaluaciones mensuales.',
    precio: 45,
    estado: 'Activo',
  },
  {
    id: '2',
    nombre: 'Membresía Smart Fit Estudiante',
    detalles: 'Acceso al área de musculación de lunes a viernes en horario matutino.',
    precio: 25,
    estado: 'Activo',
  },
]

const initialSessions: GymSession[] = [
  { id: '1', clienteId: '1', entrenadorId: '1', fecha: '2026-09-23', horaInicio: '08:00', horaFin: '09:00', estado: 'Completada' },
  { id: '2', clienteId: '2', entrenadorId: '1', fecha: '2026-09-26', horaInicio: '16:00', horaFin: '17:00', estado: 'Programada' },
]

const initialAsistencia: AsistenciaRecord[] = [
  { id: '1', clienteId: '1', estado: 'Completada', fecha: '2026-09-24', hora: '06:15' },
  { id: '2', clienteId: '2', estado: 'Completada', fecha: '2026-09-24', hora: '18:30' },
  { id: '3', clienteId: '1', estado: 'Activo', fecha: '2026-09-25', hora: '06:00' },
]

const members = ref<Member[]>(initialMembers)
const trainers = ref<Trainer[]>(initialTrainers)
const plans = ref<GymPlan[]>(initialPlans)
const sessions = ref<GymSession[]>(initialSessions)
const asistencia = ref<AsistenciaRecord[]>(initialAsistencia)
const presentMembers = ref<Member[]>([])

export function useGymData() {
  const activeMembers = computed(() => members.value.filter((member) => member.estado === 'Activo').length)
  const today = computed(() => sessions.value[sessions.value.length - 1]?.fecha || '')
  const todaySessions = computed(() => sessions.value.filter((session) => session.fecha === today.value).length)

  const memberById = (id: string) => members.value.find((member) => member.id === id)
  const trainerById = (id: string) => trainers.value.find((trainer) => trainer.id === id)
  const planById = (id: string | null) => plans.value.find((plan) => plan.id === id)
  const attendanceByMember = (id: string) => asistencia.value.filter((record) => record.clienteId === id)

  const addMember = (member: Member) => members.value.unshift(member)
  const updateMember = (id: string, changes: Partial<Member>) => {
    const member = members.value.find((item) => item.id === id)
    if (member) Object.assign(member, changes)
  }
  const changeMemberPlan = (id: string, planId: string) => {
    const member = members.value.find((item) => item.id === id)
    if (member) member.planId = planId
  }

  const addPlan = (plan: GymPlan) => plans.value.push(plan)
  const updatePlan = (id: string, changes: Omit<GymPlan, 'id'>) => {
    const plan = plans.value.find((item) => item.id === id)
    if (plan) Object.assign(plan, changes)
  }
  const deletePlan = (id: string) => {
    plans.value = plans.value.filter((item) => item.id !== id)
  }

  const addTrainer = (trainer: Trainer) => trainers.value.unshift(trainer)
  const updateTrainer = (id: string, changes: Partial<Trainer>) => {
    const trainer = trainers.value.find((item) => item.id === id)
    if (trainer) Object.assign(trainer, changes)
  }

  const addSession = (session: GymSession) => sessions.value.push(session)
  const updateSessionStatus = (id: string, estado: SessionStatus) => {
    const session = sessions.value.find((item) => item.id === id)
    if (session) session.estado = estado
  }
  const deleteSession = (id: string) => {
    sessions.value = sessions.value.filter((session) => session.id !== id)
  }

  const registerAccess = (member: Member) => {
    const estado = member.estado === 'Activo' ? 'Activo' : 'Cancelada'
    asistencia.value.unshift({
      id: `${Date.now()}`,
      clienteId: member.id,
      estado,
      fecha: new Date().toISOString().slice(0, 10),
      hora: new Date().toLocaleTimeString('es-SV', { hour: '2-digit', minute: '2-digit' }),
    })
    if (member.estado === 'Activo' && !presentMembers.value.some((item) => item.id === member.id)) {
      presentMembers.value.push(member)
    }
  }

  return {
    members,
    trainers,
    plans,
    sessions,
    asistencia,
    presentMembers,
    activeMembers,
    todaySessions,
    memberById,
    trainerById,
    planById,
    attendanceByMember,
    addMember,
    updateMember,
    changeMemberPlan,
    addPlan,
    updatePlan,
    deletePlan,
    addTrainer,
    updateTrainer,
    addSession,
    updateSessionStatus,
    deleteSession,
    registerAccess,
  }
}
