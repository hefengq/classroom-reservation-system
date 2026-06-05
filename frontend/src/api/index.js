import http from './http'

export const login = (payload) => http.post('/auth/login', payload)
export const listClassrooms = () => http.get('/classrooms')
export const createClassroom = (payload) => http.post('/classrooms', payload)
export const updateClassroom = (id, payload) => http.put(`/classrooms/${id}`, payload)
export const deleteClassroom = (id) => http.delete(`/classrooms/${id}`)
export const board = (params) => http.get('/classrooms/board', { params })
export const applyReservation = (payload) => http.post('/reservations/apply', payload)
export const myReservations = () => http.get('/reservations/my')
export const pendingReservations = () => http.get('/admin/reservations/pending')
export const auditReservation = (id, payload) => http.put(`/admin/reservations/audit/${id}`, payload)
export const createUser = (payload) => http.post('/admin/users', payload)
