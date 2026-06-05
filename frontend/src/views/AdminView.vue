<template>
  <section class="page">
    <div class="page-title">
      <h2>管理后台</h2>
      <el-button type="primary" @click="loadPending">刷新待审批</el-button>
    </div>
    <el-tabs>
      <el-tab-pane label="预约审批">
        <el-table :data="pending" border>
          <el-table-column prop="applicant" label="申请人" width="120" />
          <el-table-column prop="roomNumber" label="教室" width="120" />
          <el-table-column prop="reservationDate" label="日期" width="130" />
          <el-table-column label="节次" width="110">
            <template #default="{ row }">{{ row.slotStart }} - {{ row.slotEnd }}</template>
          </el-table-column>
          <el-table-column prop="purpose" label="用途" />
          <el-table-column label="操作" width="210">
            <template #default="{ row }">
              <el-button size="small" type="success" @click="audit(row.id, 1)">通过</el-button>
              <el-button size="small" type="danger" @click="reject(row.id)">驳回</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="教室管理">
        <div class="toolbar">
          <el-input v-model="roomForm.roomNumber" placeholder="教室编号" style="width:160px" />
          <el-input-number v-model="roomForm.capacity" :min="1" />
          <el-input v-model="roomForm.equipment" placeholder="设备" style="width:260px" />
          <el-button type="primary" @click="saveRoom">新增教室</el-button>
        </div>
        <el-table :data="classrooms" border>
          <el-table-column prop="roomNumber" label="教室编号" />
          <el-table-column prop="capacity" label="容量" />
          <el-table-column prop="equipment" label="设备" />
          <el-table-column label="状态">
            <template #default="{ row }"><el-tag>{{ row.status === 1 ? '可用' : '停用' }}</el-tag></template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="用户管理">
        <el-form :model="userForm" inline>
          <el-form-item><el-input v-model="userForm.username" placeholder="账号" /></el-form-item>
          <el-form-item><el-input v-model="userForm.password" placeholder="初始密码" /></el-form-item>
          <el-form-item><el-input v-model="userForm.nickname" placeholder="姓名" /></el-form-item>
          <el-form-item><el-input v-model="userForm.email" placeholder="邮箱" /></el-form-item>
          <el-form-item><el-button type="primary" @click="saveUser">创建用户</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { auditReservation, createClassroom, createUser, listClassrooms, pendingReservations } from '../api'

const pending = ref([])
const classrooms = ref([])
const roomForm = reactive({ roomNumber: '', capacity: 60, equipment: '', status: 1 })
const userForm = reactive({ username: '', password: '123456', nickname: '', email: '', role: 'USER' })

onMounted(async () => {
  await Promise.all([loadPending(), loadRooms()])
})

async function loadPending() { pending.value = await pendingReservations() }
async function loadRooms() { classrooms.value = await listClassrooms() }

async function audit(id, status, rejectReason = '') {
  await auditReservation(id, { status, rejectReason })
  ElMessage.success('审批完成')
  await loadPending()
}

async function reject(id) {
  const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回预约')
  await audit(id, 2, value)
}

async function saveRoom() {
  await createClassroom(roomForm)
  ElMessage.success('教室已创建')
  Object.assign(roomForm, { roomNumber: '', capacity: 60, equipment: '', status: 1 })
  await loadRooms()
}

async function saveUser() {
  await createUser(userForm)
  ElMessage.success('用户已创建')
}
</script>
