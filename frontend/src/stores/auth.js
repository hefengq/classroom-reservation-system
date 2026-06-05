import { defineStore } from 'pinia'
import { login } from '../api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    role: localStorage.getItem('role') || '',
    userId: localStorage.getItem('userId') || '',
    nickname: localStorage.getItem('nickname') || ''
  }),
  actions: {
    async signIn(payload) {
      const data = await login(payload)
      this.token = data.token
      this.role = data.role
      this.userId = String(data.userId)
      this.nickname = data.nickname
      localStorage.setItem('token', data.token)
      localStorage.setItem('role', data.role)
      localStorage.setItem('userId', String(data.userId))
      localStorage.setItem('nickname', data.nickname)
    },
    logout() {
      this.token = ''
      this.role = ''
      this.userId = ''
      this.nickname = ''
      localStorage.clear()
    }
  }
})
