import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({ baseURL: '/api', timeout: 12000 })

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use((response) => {
  const body = response.data
  if (body.code !== 200) {
    ElMessage.error(body.message || '请求失败')
    return Promise.reject(new Error(body.message))
  }
  return body.data
})

export default http
