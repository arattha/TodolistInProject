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
        h-5/6
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
            h-1/6
            w-full
            justify-center
            items-center
            text-white
            font-black
            text-3xl
          "
        >
          할일 보내기
        </div>
        <div class="flex flex-col h-full w-full bg-itemGray overflow-y-auto">
          <div class="flex flex-col justify-center items-center py-10">
            <div class="flex justify-between mb-3 w-10/12 lg:w-7/12">
              <div class="flex items-center font-black w-4/12 text-xl">팀</div>
              <div class="flex flex-col w-8/12">
                <select
                  class="
                    bg-contentGray
                    flex-1
                    border border-transparent
                    py-2
                    px-4
                    text-gray-700
                    placeholder-gray-400
                    shadow-md
                    rounded-lg
                    focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
                  "
                  v-model="selectTeam"
                  @change="inputTeam"
                >
                  <option disabled value="" hidden>팀을 선택해주세요.</option>
                  <option v-for="(team, index) in teamList" :key="index" :value="team">
                    {{ team.teamName }}
                  </option>
                </select>

                <p class="text-red-500 font-black text-xs mt-2" v-if="!isValid">
                  팀을 선택해주세요.
                </p>
              </div>
            </div>
          </div>
          <input
            class="
              mx-8
              mb-1
              lg:mx-16
              w-7/12
              bg-contentGray
              flex-1
              appearance-none
              border border-transparent
              py-2
              px-4
              text-gray-700
              placeholder-gray-400
              rounded-lg
              shadow-md
              text-base
              focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
            "
            type="text"
            :value="searchName"
            @input="typingSearchName"
            placeholder="팀원의 이름을 검색하세요."
          />
          <div
            class="
              flex flex-col
              h-full
              bg-contentGray
              mx-8
              lg:mx-16
              rounded-md
              shadow-lg
              overflow-y-auto
            "
          >
            <div class="flex w-full border-b-2 border-itemGray p-3">
              <div class="flex justify-center items-center w-1/12 font-black text-xl"></div>
              <div class="flex justify-center items-center w-8/12 font-black text-xl">이메일</div>
              <div class="flex justify-center items-center w-3/12 font-black text-xl">이름</div>
            </div>

            <div class="flex flex-col overflow-y-auto scroll_type2">
              <div class="flex pb-2" v-for="(member, index) in searchByMemberName" :key="index">
                <div class="bg-contentGray w-full hover:bg-itemGray">
                  <label :for="member.id" class="flex items-center px-2 py-5 cursor-pointer">
                    <input
                      type="radio"
                      v-model="checkedMember"
                      :value="member"
                      :id="member.id"
                      @click="uncheck(member)"
                      class="
                        w-1/12
                        form-tick
                        h-6
                        border border-gray-300
                        rounded-md
                        checked:bg-blue-600 checked:border-transparent
                        focus:outline-none
                      "
                    />
                    <div class="flex justify-center items-center w-8/12">{{ member.email }}</div>
                    <div class="flex justify-center items-center w-3/12">{{ member.name }}</div>
                  </label>
                </div>
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
            @click="sendTodo"
          >
            보내기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import vClickOutside from 'v-click-outside';
import { getMembersByTeam } from '@/api/auth.js';
import { getTeam } from '@/api/team.js';
import { mapGetters } from 'vuex';

export default {
  name: 'TODOMOVEMODAL',
  props: ['todoInfo', 'isDetail', 'stomp', 'selectedTeam'],
  data() {
    return {
      teamList: [],
      selectTeam: '',
      searchName: '',
      memberList: [],
      checkedMember: [],
      isValid: false,
      isSend: false,
    };
  },
  directives: {
    clickOutside: vClickOutside.directive,
  },
  created() {
    // 팀을 선택하면 멤버를 부를지 멤버를 선택하면 팀을 부를지는 로직 개발자가 선택할 것
    // 여기서는 팀을 선택하면 멤버를 선택한다고 가정하고 페이지를 구성
    this.setDefault();

    getTeam(
      this.projectId,
      (res) => {
        // team 가져옴
        this.teamList = [];

        res.object.forEach((value) => {
          this.teamList.push({
            teamId: value.id,
            teamName: value.name
          });
        });

      },
      () => {
        console.log('team 가져오기 실패');
      }
    );
    
    
  },
  methods: {
    onClickOutside() {
      this.closeModal();
    },
    closeModal() {
      if(this.isSend){
        this.$emit('closeModal');
      } else {
        this.$emit('justCloseModal');
      }
    },
    typingSearchName(e) {
      this.searchName = e.target.value;
    },
    setDefault(){
      if(this.selectedTeam != null){
        this.isValid = true;
        this.selectTeam = this.selectedTeam;
        this.inputTeam();
      }
    },
    inputTeam() {
      if (this.selectTeam == []) {
        this.isValid = false;
        return;
      }


      // 선택된 팀원의 값은 초기화 해야한다.
      this.checkedMember = [];

      // this.selectTema에 팀명이 담기게 되므로 이를 이용해서 백엔드에서
      // 해당 팀의 팀원 리스트를 아래 memberList에 담으면 된다.
      getMembersByTeam(
        this.selectTeam.teamId,
        (res) => {
          this.memberList = res.object;
        },
        () => {}
      );

      // this.memberList = [
      //   {
      //     id: 'cyi',
      //     email: 'cyi@naver.com',
      //     name: '조용일',
      //   }
      // ];

      this.isValid = true;
      
    },
    async sendTodo() {
      // 팀 이름 입력이 필수!
      if (!this.isValid) {
        return;
      }
      this.isSend = true;
      // selectTeam에는 보내고자하는 팀이
      // checkMember에는 보내고자하는 팀원이 들어간다.
      let tmp = this.todoInfo;
      tmp.status = "접수";
      tmp.teamId = this.selectTeam.teamId;
      tmp.teamName = this.selectTeam.teamName;
      tmp.memberId = this.checkedMember.id;
      tmp.memberName = this.checkedMember.name;
      
      if (this.checkedMember.length === 0) {
        tmp.status = "New";
        tmp.memberId = null;
        tmp.memberName = null;
        await this.stomp.send(
          '/server/moveTodo/team',
          JSON.stringify(tmp),
          {}
        );

      } else {
        await this.stomp.send(
          '/server/moveTodo/team',
          JSON.stringify(tmp),
          {}
        );
      }

      this.closeModal();
    },
    uncheck(val) {
      if (val === this.checkedMember) {
        this.checkedMember = [];
      } else {
        this.checkedMember = val;
      }
    },
  },
  computed: {
    ...mapGetters(['projectId', 'id']),
    searchByMemberName() {
      return this.memberList.filter((member) => {
        return member.name.includes(this.searchName);
      });
    },
  },
};
</script>
