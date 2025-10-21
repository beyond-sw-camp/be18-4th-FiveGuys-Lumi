<template>
  <div>
    <!-- 🔹 참여자 목록 -->

    <v-sheet class="pa-4">
      <!-- 제목 + 버튼 -->
      <div class="d-flex align-center justify-space-between mb-3">
        <h3 class="text-h6 font-weight-bold">참여자 목록</h3>
        <v-btn
          v-if="myRole === 'TUTOR'"
          class="rounded-xl"
          color="primary-button-1"
          elevation="0"
          @click="openDialog"
          >초대 발송하기</v-btn
        >
      </div>

      <!-- 참여자 리스트 -->
      <v-table class="mt-4">
        <thead>
          <tr>
            <th class="text-left">이름</th>
            <th class="text-left">이메일</th>
            <th class="text-left">역할</th>
            <th class="text-right">액션</th>
          </tr>
        </thead>

        <tbody>
          <!-- 참여자 목록 -->
          <tr v-for="user in participants" :key="user.userId">
            <td>{{ user.name || user.userId }}</td>
            <td>{{ user.email || user.userId }}</td>
            <td>{{ user.roleName }}</td>
            <td class="text-right">
              <template
                v-if="user.userId === authStore.tokenInfo.userId && user.roleName !== 'TUTOR'"
              >
                <!-- <v-btn color="#eeddff" size="small" @click="openUserInfo(user)">정보</v-btn> -->
                <v-btn color="#eeddff" size="small" @click="handleAction(user)">탈퇴</v-btn>
              </template>
            </td>
          </tr>

          <!-- 참여자가 없을 때 -->
          <tr v-if="participants.length === 0">
            <td class="text-grey text-center" colspan="4">참여자가 없습니다.</td>
          </tr>
        </tbody>
      </v-table>
    </v-sheet>

    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title class="text-h6">
          {{ channel?.name || '초대 발송' }}
        </v-card-title>

        <v-divider class="my-2" />

        <!-- 발송 성공 후 코드 보여주기 -->
        <div v-if="invitationCode" class="pa-4">
          <p class="text-subtitle-1">
            ✅ 초대 코드:
            <strong>
              {{ invitationCode }}<br />
              유효기간 7일입니다.
            </strong>
          </p>
        </div>

        <!-- 발송 전일 때만 역할 선택 보이도록 -->
        <div v-else>
          <v-select
            v-model="selectedRoleId"
            hide-details
            item-title="name"
            item-value="id"
            :items="roles"
            label="역할 선택"
            outlined
          />
        </div>

        <v-card-actions class="justify-end">
          <v-btn text @click="closeDialog">취소</v-btn>
          <v-btn
            v-if="!invitationCode"
            color="primary"
            :disabled="!selectedRoleId"
            @click="sendInvitation"
          >
            발송하기
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 🔹 참여자 정보 모달 (조회 전용) -->
    <ChannelUserInfoModal
      :channel-id="channel?.id || $route.params.channelId"
      :user-id="selectedUserId"
      :visible="userInfoDialog"
      @close="userInfoDialog = false"
    />
  </div>
</template>

<script>
import { getChannel } from '@/apis/channel';
import ChannelUserInfoModal from '@/pages/channel/components/ChannelUserInfoModal.vue';
import { useAuthStore } from '@/stores/authStore';

export default {
  name: 'InviteDialog',
  components: { ChannelUserInfoModal },
  props: {
    channel: {
      type: Object,
      required: false,
      default: () => ({ name: '', subject: '' }),
    },
  },
  setup() {
    const authStore = useAuthStore();
    return { authStore };
  },
  data() {
    return {
      dialog: false,
      selectedRoleId: null,
      myRole: null,
      invitationCode: null,
      participants: [],
      roles: [
        { id: 2, name: '학생' },
        { id: 3, name: '학부모' },
      ],
      // InfoDialog 제어용 상태
      userInfoDialog: false,
      selectedUserId: null,
    };
  },
  async mounted() {
    await this.loadParticipants();
    await this.loadChannelRole();
  },
  methods: {
    async openDialog() {
      this.dialog = true;
    },
    async loadParticipants() {
      try {
        const channelId = this.$route.params.channelId;
        this.participants = await this.authStore.fetchParticipants(channelId);
      } catch (error) {
        console.error(error);
      }
    },
    async loadChannelRole() {
      try {
        const channelId = this.$route.params.channelId;
        const channelData = await getChannel(channelId);
        this.myRole = channelData.roleName; // ✅ 내 역할 저장
      } catch (error) {
        console.error('채널 역할 조회 실패:', error);
      }
    },
    async sendInvitation() {
      try {
        const channelId = this.$route.params.channelId;
        const result = await this.authStore.sendInvitation(channelId, this.selectedRoleId);

        if (result?.data?.[0]?.invitationCode) {
          this.invitationCode = result.data[0].invitationCode;
        }

        this.selectedRoleId = null;
        await this.loadParticipants();
      } catch (error) {
        console.error(error);
      }
    },
    closeDialog() {
      this.dialog = false;
      this.invitationCode = null;
    },
    async handleAction(user) {
      try {
        if (user.userId !== this.authStore.tokenInfo.userId) return;

        const channelId = this.$route.params.channelId;
        const response = await this.authStore.deleteSelfFromChannel(channelId);

        this.$router.replace('/channels');
      } catch (error) {
        console.error('참여자 삭제 실패:', error);
      }
    },
    openUserInfo(user) {
      this.selectedUserId = Array.isArray(user.userId) ? user.userId[0] : user.userId;
      this.userInfoDialog = true;
    },
  },
};
</script>
