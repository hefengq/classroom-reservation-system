<template>
  <section class="page">
    <div class="page-title">
      <h2>可视化预约看板</h2>
    </div>
    <div class="toolbar">
      <el-date-picker v-model="start" type="date" value-format="YYYY-MM-DD" />
      <el-select v-model="classroomId" clearable placeholder="全部教室" style="width:180px">
        <el-option v-for="room in classrooms" :key="room.id" :label="room.roomNumber" :value="room.id" />
      </el-select>
      <el-button type="primary" @click="load">刷新</el-button>
    </div>
    <div class="board">
      <div class="grid">
        <div class="cell head">教室 / 节次</div>
        <div v-for="day in days" :key="day" class="cell head">{{ day }}</div>
        <template v-for="room in visibleClassrooms" :key="room.id">
          <template v-for="slot in slots" :key="`${room.id}-${slot}`">
            <div class="cell slot">
              <strong>{{ room.roomNumber }}</strong>
              <span>第 {{ slot }} 节</span>
            </div>
            <div
              v-for="day in days"
              :key="`${room.id}-${day}-${slot}`"
              class="cell free"
              :class="cellClass(day, slot, room.id)"
              @click="openApply(day, slot, room.id)"
            >
              <template v-if="findReservation(day, slot, room.id)">
                <strong>{{ findReservation(day, slot, room.id).purpose }}</strong>
                <div>{{ findReservation(day, slot, room.id).status === 0 ? '待审批' : '已通过' }}</div>
              </template>
              <span v-else>可预约</span>
            </div>
          </template>
        </template>
        <template v-if="visibleClassrooms.length === 0">
          <div class="cell empty" :style="{ gridColumn: `1 / span ${days.length + 1}` }">暂无教室</div>
        </template>
      </div>
    </div>
    <el-dialog v-model="dialog" title="提交预约" width="420px">
      <el-form :model="applyForm" label-position="top">
        <el-form-item label="教室">
          <el-select v-model="applyForm.classroomId" style="width:100%">
            <el-option v-for="room in classrooms" :key="room.id" :label="room.roomNumber" :value="room.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="用途">
          <el-input v-model="applyForm.purpose" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog=false">取消</el-button>
        <el-button type="primary" @click="submitApply">提交</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { applyReservation, board, listClassrooms } from '../api'

const slots = Array.from({ length: 10 }, (_, i) => i + 1)
const start = ref(new Date().toISOString().slice(0, 10))
const classroomId = ref()
const classrooms = ref([])
const reservations = ref([])
const dialog = ref(false)
const applyForm = reactive({ classroomId: null, reservationDate: '', slotStart: 1, slotEnd: 1, purpose: '' })

const days = computed(() => {
  const base = new Date(start.value)
  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(base)
    d.setDate(base.getDate() + i)
    return d.toISOString().slice(0, 10)
  })
})
const visibleClassrooms = computed(() => {
  return classroomId.value
    ? classrooms.value.filter((room) => room.id === classroomId.value)
    : classrooms.value
})

onMounted(async () => {
  classrooms.value = await listClassrooms()
  await load()
})

async function load() {
  reservations.value = await board({ startDate: days.value[0], endDate: days.value[6], classroomId: classroomId.value })
}

function findReservation(day, slot, roomId) {
  return reservations.value.find((item) => (
    item.classroomId === roomId
    && item.reservationDate === day
    && item.slotStart <= slot
    && item.slotEnd >= slot
  ))
}

function cellClass(day, slot, roomId) {
  const item = findReservation(day, slot, roomId)
  return item ? (item.status === 0 ? 'occupied pending' : 'occupied') : ''
}

function openApply(day, slot, roomId) {
  if (findReservation(day, slot, roomId)) {
    ElMessage.warning('该时段已有预约')
    return
  }
  applyForm.classroomId = roomId
  applyForm.reservationDate = day
  applyForm.slotStart = slot
  applyForm.slotEnd = slot
  applyForm.purpose = ''
  dialog.value = true
}

async function submitApply() {
  await applyReservation(applyForm)
  ElMessage.success('预约申请已提交')
  dialog.value = false
  await load()
}
</script>
