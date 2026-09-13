import { computed, ref } from 'vue'

export type MemberStatus = 'Activo' | 'Por vencer' | 'Vencido' | 'Inactivo'
export type SessionStatus = 'Programada' | 'Confirmada' | 'Completada' | 'Cancelada'

export type PlanColor = 'blue' | 'lime' | 'orange' | 'violet' | 'red'

export interface GymPlan {
  id: string
  name: string
  price: number
  duration: string
  features: string[]
  color: PlanColor
  isBase: boolean
}

export interface Member {
  id: string
  name: string
  email: string
  phone: string
  plan: string
  startDate: string
  expiry: string
  status: MemberStatus
  trainer: string
  initials: string
  attendance: string[]
  address: string
  birthDate: string
  notes: string
  qrCode: string
}

export interface Trainer {
  id: string
  name: string
  email: string
  phone: string
  specialty: string
  schedule: string
  rate: number
  rating: number
  clients: number
  active: boolean
  initials: string
  bio: string
}

export interface GymSession {
  id: string
  memberId: string
  trainerId: string
  date: string
  time: string
  duration: number
  type: string
  status: SessionStatus
  rate: number
  notes: string
}

const initialMembers: Member[] = [
  { id: 'm1', name: 'Valentina Ríos', email: 'valentina@correo.com', phone: '555 010 2233', plan: 'Mensual Plus', startDate: '2026-05-14', expiry: '2026-07-20', status: 'Por vencer', trainer: 'Diego Ramírez', initials: 'VR', attendance: ['2026-07-01', '2026-07-03', '2026-07-07', '2026-07-10'], address: 'Av. Central 123', birthDate: '1995-07-22', notes: 'Objetivo: ganar masa muscular.', qrCode: 'PU-M1-VR' },
  { id: 'm2', name: 'Mateo Fernández', email: 'mateo@correo.com', phone: '555 010 9912', plan: 'Mensual Básico', startDate: '2026-01-05', expiry: '2026-08-05', status: 'Activo', trainer: 'Sofía Luna', initials: 'MF', attendance: ['2026-07-02', '2026-07-04', '2026-07-08'], address: 'Calle Norte 45', birthDate: '1991-02-18', notes: '', qrCode: 'PU-M2-MF' },
  { id: 'm3', name: 'Camila Torres', email: 'camila@correo.com', phone: '555 010 3818', plan: 'Corporativo', startDate: '2026-06-01', expiry: '2026-07-01', status: 'Vencido', trainer: 'Diego Ramírez', initials: 'CT', attendance: ['2026-06-28', '2026-06-30'], address: 'Paseo Sur 82', birthDate: '1997-11-09', notes: 'Alergia a ibuprofeno.', qrCode: 'PU-M3-CT' },
  { id: 'm4', name: 'Nicolás Herrera', email: 'nicolas@correo.com', phone: '555 010 4481', plan: 'Mensual Plus', startDate: '2026-04-22', expiry: '2026-07-22', status: 'Por vencer', trainer: 'Sofía Luna', initials: 'NH', attendance: ['2026-07-01', '2026-07-05', '2026-07-09'], address: 'Libertad 210', birthDate: '1993-08-12', notes: '', qrCode: 'PU-M4-NH' },
  { id: 'm5', name: 'Sofía Castillo', email: 'sofia@correo.com', phone: '555 010 7762', plan: 'Mensual Básico', startDate: '2026-07-02', expiry: '2026-08-02', status: 'Activo', trainer: 'Diego Ramírez', initials: 'SC', attendance: ['2026-07-02', '2026-07-06', '2026-07-11'], address: 'Los Álamos 17', birthDate: '1999-03-27', notes: '', qrCode: 'PU-M5-SC' },
]

const initialTrainers: Trainer[] = [
  { id: 't1', name: 'Diego Ramírez', email: 'diego@plusultra.com', phone: '555 010 1100', specialty: 'Fuerza y Powerlifting', schedule: 'Lun a Vie · 06:00 a 14:00', rate: 450, rating: 4.9, clients: 18, active: true, initials: 'DR', bio: 'Entrenador certificado con 8 años de experiencia en fuerza, acondicionamiento y preparación deportiva.' },
  { id: 't2', name: 'Sofía Luna', email: 'sofia@plusultra.com', phone: '555 010 2200', specialty: 'HIIT y Funcional', schedule: 'Lun a Sáb · 14:00 a 22:00', rate: 380, rating: 4.8, clients: 14, active: true, initials: 'SL', bio: 'Especialista en entrenamiento funcional y programas de rendimiento para todos los niveles.' },
  { id: 't3', name: 'Andrés Vega', email: 'andres@plusultra.com', phone: '555 010 3300', specialty: 'Movilidad y Rehabilitación', schedule: 'Mar a Sáb · 08:00 a 16:00', rate: 520, rating: 4.7, clients: 9, active: false, initials: 'AV', bio: 'Fisioterapeuta y entrenador enfocado en movilidad, prevención y regreso seguro al entrenamiento.' },
]

const members = ref<Member[]>(initialMembers)
const trainers = ref<Trainer[]>(initialTrainers)
const sessions = ref<GymSession[]>([
  { id: 's1', memberId: 'm1', trainerId: 't1', date: '2026-07-14', time: '08:00', duration: 60, type: 'Powerlifting', status: 'Confirmada', rate: 450, notes: 'Trabajo de sentadilla.' },
  { id: 's2', memberId: 'm2', trainerId: 't2', date: '2026-07-14', time: '10:30', duration: 45, type: 'HIIT', status: 'Programada', rate: 285, notes: '' },
  { id: 's3', memberId: 'm4', trainerId: 't1', date: '2026-07-15', time: '17:00', duration: 60, type: 'Musculación', status: 'Completada', rate: 450, notes: 'Aumentar carga progresivamente.' },
  { id: 's4', memberId: 'm5', trainerId: 't2', date: '2026-07-16', time: '19:00', duration: 45, type: 'Funcional', status: 'Programada', rate: 285, notes: '' },
])

const accessLog = ref([
  { id: 'a1', member: 'Valentina Ríos', type: 'Entrada', time: '07:12', status: 'OK' },
  { id: 'a2', member: 'Mateo Fernández', type: 'Entrada', time: '07:34', status: 'OK' },
  { id: 'a3', member: 'Camila Torres', type: 'Entrada', time: '07:51', status: 'Error' },
])

const plans = ref<GymPlan[]>([
  { id: 'p1', name: 'Mensual Básico', price: 8500, duration: '1 mes', features: ['Acceso sala principal', 'Casillero incluido', 'Horario completo', 'Ducha incluida'], color: 'blue', isBase: true },
  { id: 'p2', name: 'Mensual Plus', price: 12800, duration: '1 mes', features: ['Todo Básico', 'Clases grupales ilimitadas', '1 sesión con entrenador', 'App de seguimiento'], color: 'lime', isBase: true },
  { id: 'p3', name: 'Anual Premium', price: 98000, duration: '12 meses', features: ['Todo Plus', 'Sesiones personales ilimitadas', 'Evaluación mensual', 'Nutrición deportiva'], color: 'orange', isBase: true },
])

export function useGymData() {
  const activeMembers = computed(() => members.value.filter((member) => member.status === 'Activo' || member.status === 'Por vencer').length)
  const todaySessions = computed(() => sessions.value.filter((session) => session.date === '2026-07-14').length)
  const presentMembers = ref<Member[]>(members.value.slice(0, 2))

  const memberById = (id: string) => members.value.find((member) => member.id === id)
  const trainerById = (id: string) => trainers.value.find((trainer) => trainer.id === id)

  const addMember = (member: Member) => members.value.unshift(member)
  const updateMember = (id: string, changes: Partial<Member>) => {
    const member = members.value.find((item) => item.id === id)
    if (member) Object.assign(member, changes)
  }
  const changeMemberPlan = (id: string, plan: GymPlan) => {
    const member = members.value.find((item) => item.id === id)
    if (!member) return
    member.plan = plan.name
    member.startDate = new Date().toISOString().slice(0, 10)
    member.expiry = plan.duration === '12 meses' ? `${new Date().getFullYear() + 1}-07-14` : '2026-08-14'
    member.status = 'Activo'
  }
  const addPlan = (plan: GymPlan) => plans.value.push(plan)
  const updatePlan = (id: string, changes: Omit<GymPlan, 'id' | 'isBase'>) => {
    const plan = plans.value.find((item) => item.id === id)
    if (plan) Object.assign(plan, changes)
  }
  const deletePlan = (id: string) => {
    const plan = plans.value.find((item) => item.id === id)
    if (plan && !plan.isBase) plans.value = plans.value.filter((item) => item.id !== id)
  }
  const addSession = (session: GymSession) => sessions.value.push(session)
  const updateSessionStatus = (id: string, status: SessionStatus) => {
    const session = sessions.value.find((item) => item.id === id)
    if (session) session.status = status
  }
  const updateSessionNotes = (id: string, notes: string) => {
    const session = sessions.value.find((item) => item.id === id)
    if (session) session.notes = notes
  }
  const deleteSession = (id: string) => {
    sessions.value = sessions.value.filter((session) => session.id !== id)
  }
  const registerAccess = (member: Member, type: 'Entrada' | 'Salida' = 'Entrada') => {
    accessLog.value.unshift({ id: `a${Date.now()}`, member: member.name, type, time: new Date().toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' }), status: member.status === 'Vencido' ? 'Error' : 'OK' })
    if (type === 'Entrada' && member.status !== 'Vencido' && !presentMembers.value.some((item) => item.id === member.id)) presentMembers.value.push(member)
  }

  return {
    members,
    trainers,
    sessions,
    accessLog,
    plans,
    activeMembers,
    todaySessions,
    presentMembers,
    memberById,
    trainerById,
    addMember,
    updateMember,
    changeMemberPlan,
    addPlan,
    updatePlan,
    deletePlan,
    addSession,
    updateSessionStatus,
    updateSessionNotes,
    deleteSession,
    registerAccess,
  }
}
