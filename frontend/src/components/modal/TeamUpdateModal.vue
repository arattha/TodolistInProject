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
        h-1/2
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
          팀 이름 수정
        </div>
        <div
          class="
            flex flex-col
            items-center
            justify-center
            h-full
            w-full
            bg-itemGray
            overflow-y-auto
          "
        >
          <input
            class="
              w-11/12
              bg-contentGray
              flex
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
            placeholder="변경할 팀의 이름을 적어주세요."
            v-model="teamName"
            @input="typingTeamName"
            type="text"
          />
          <p class="text-red-500 font-black text-xs mt-2" v-if="!isValid">
            팀 이름이 중복이거나 비어있습니다.
          </p>
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
            @click="teamUpdate"
          >
            수정
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import vClickOutside from 'v-click-outside';
import { mapGetters } from 'vuex';
import { modifyProjectTeamName, teamNameCheck } from '@/api/team.js';

export default {
  name: 'TEAMUPDATEMODAL',
  data() {
    return {
      teamName: '',
      isValid: false,
    };
  },
  props: ['todoInfo', 'stomp', 'teamId'],
  directives: {
    clickOutside: vClickOutside.directive,
  },
  computed:{
      ...mapGetters(['projectId'])
  },
  methods: {
    onClickOutside() {
      this.closeModal();
    },
    closeModal() {
      this.$emit('closeTeamNameModal');
    },
    typingTeamName(e) {
      if (!e.target.value) {
        this.isValid = false;
        return;
      }
      teamNameCheck(
        {
          projectId : this.$route.params.projectId,
          teamName : this.teamName,
        },
        (res) => {
          if(res.status){
            this.isValid = true;
          } else {
            this.isValid = false;
          }
        },
        (error) => {
          console.log(error);
        }
      )
    },
    teamUpdate() {
      // 팀 이름 입력이 필수!
      if (!this.isValid) {
        return;
      }

      modifyProjectTeamName({
          id: this.teamId,
          name: this.teamName,
          projectId: this.projectId
      },
      () => {},
      () => {});

      this.closeModal();
    },
  },
};
</script>
