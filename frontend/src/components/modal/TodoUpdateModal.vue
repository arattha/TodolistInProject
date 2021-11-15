<template>
  <div
    class="
      flex
      justify-center
      items-center
      fixed
      top-0
      left-0
      right-0
      bottom-0
      w-screen
      h-screen
      z-50
      bg-buttonGray bg-opacity-75
    "
  >
    <div
      class="
        flex flex-col
        h-2/6
        lg:w-5/12
        w-9/12
        mx-auto
        rounded-md
        shadow-lg
        fixed
        translate-x-1/2 translate-y-1/2
        bg-white
        z-50
      "
      v-click-outside="onClickOutside"
    >
      <div class="flex flex-col h-full w-full">
        <div
          class="
            flex
            rounded-t-md
            bg-headerGray
            h-2/6
            w-full
            justify-center
            items-center
            text-white
            font-black
            text-3xl
          "
        >
          할일 수정
        </div>
        <div class="flex flex-col h-full w-full bg-itemGray overflow-y-auto">
          <div class="flex flex-col justify-center items-center py-10">
            <div class="flex justify-between mb-3 w-10/12 lg:w-8/12">
              <div class="flex items-center font-black w-4/12 text-2xl">할일</div>
              <div class="flex flex-col w-10/12">
                <input
                  class="
                    bg-contentGray
                    flex-1
                    appearance-none
                    border border-transparent
                    py-2
                    px-4
                    text-gray-700
                    placeholder-gray-400
                    shadow-md
                    rounded-lg
                    focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
                  "
                  placeholder="할일의 이름을 적어주세요."
                  v-model="todoName"
                  @input="typingTodoName"
                  type="text"
                />
                <p class="text-red-500 font-black text-xs mt-2" v-if="!isValid">
                  할일의 이름을 입력해주세요.
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="flex justify-center items-center p-5 bg-itemGray">
          <button
            class="
              bg-red-600
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-red-800
              focus:outline-none
              focus:ring-2
              focus:ring-red-500
              focus:ring-offset-2
              focus:ring-offset-red-200
              mr-10
            "
            @click="closeModal"
          >
            닫기
          </button>
          <button
            class="
              bg-headerGray
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-menuGray
              focus:outline-none
              focus:ring-2
              focus:ring-blue-500
              focus:ring-offset-2
              focus:ring-offset-blue-200
            "
            @click="todoUpdate"
          >
            추가
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import vClickOutside from 'v-click-outside';
import { mapGetters } from 'vuex';
import { getMembersByTeam } from '@/api/auth.js';

export default {
  name: 'TODOUPDATEMODAL',
  data() {
    return {
      todoName: '',
      isValid: false,
    };
  },
  props: ['todoInfo'],
  directives: {
    clickOutside: vClickOutside.directive,
  },
  created() {
    getMembersByTeam(
      this.teamId,
      (res) => {
        this.memberList = res.object;
      },
      (error) => {
        console.error(error);
      }
    );
  },
  methods: {
    onClickOutside() {
      this.closeModal();
    },
    closeModal() {
      this.$emit('closeModal');
    },
    typingTodoName(e) {
      if (!e.target.value) {
        this.isValid = false;
        return;
      }

      this.isValid = true;
    },
    todoUpdate() {
      // 팀 이름 입력이 필수!
      if (!this.isValid) {
        return;
      }
      this.stomp.send(
        '/server/updateTodo',
        JSON.stringify({
          id: this.todoInfo.id,
          title: this.todoName,
          status: this.todoInfo.status,
          memberId: this.todoInfo.memberId,
          projectId: this.todoInfo.projectId,
          teamId: this.todoInfo.teamId,
        })
      );
      this.closeModal();
    },
  },
  computed: {
    ...mapGetters(['stomp']),
  },
};
</script>
