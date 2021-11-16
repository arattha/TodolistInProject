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
          팀 추가
        </div>
        <div class="flex flex-col h-full w-full bg-itemGray overflow-y-auto">
          <div class="flex flex-col justify-center items-center py-10">
            <div class="flex justify-between mb-3 w-10/12 lg:w-7/12">
              <div class="flex items-center font-black w-4/12 text-xl">팀 이름</div>
              <div class="flex flex-col w-8/12">
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
                  :disabled="isModify"
                  placeholder="팀 이름을 적어주세요."
                  v-model="teamName"
                  @input="typingTeamName"
                  type="text"
                />

                <p class="text-red-500 font-black text-xs mt-2" v-if="!isValid">
                  팀 이름이 중복이거나 비어있습니다.
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
                      type="checkbox"
                      v-model="checkedMember"
                      :value="member.id"
                      :id="member.id"
                      class="
                        w-1/12
                        form-tick
                        h-6
                        border border-gray-300
                        rounded-md
                        checked:bg-blue-600 checked:border-transparent
                        focus:outline-none
                      "
                      @change="testMember($event)"
                    />
                    <div class="flex justify-center items-center w-8/12">{{ member.memberDetailDto.email }}</div>
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
            @click="teamAdd"
          >
            {{ isModify ? "수정" : "추가" }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import vClickOutside from 'v-click-outside';
import { getAllMembers } from '@/api/auth.js';
import { getProjectMembers, createTeam, modifyTeam, teamNameCheck } from '@/api/team.js';
import { mapGetters } from 'vuex';

export default {
  name: 'TEADADDMODAL',
  props: ['selectTeam','modifyMemberList'],
  data() {
    return {
      inputContent: '',
      beforeContent: '',
      teamName: '',
      searchName: '',
      memberList: [],
      projectMembers: [],
      checkedMember: [],
      isValid: false,
      isModify: false,
    };
  },
  directives: {
    clickOutside: vClickOutside.directive,
  },
  created() {
    this.getMemberList();
    this.getProjectMembers();
  },
  methods: {
    checkAddOrModify(){
      if(this.modifyMemberList == null) {
        this.isModify = false;
        return;
      } else {
        this.teamName = this.selectTeam.teamName;
        this.isModify = true;
        this.isValid = true;
        this.modifyMemberList.forEach((member) => {
          this.checkedMember.push(member.id);
        })
      }
    },
    testMember(e){
      if(e.target.checked){
        let clickedMember = null;
        let flag = false;

        this.memberList.forEach(m => {
            if(m.id == e.target._value){
              clickedMember = m;
            }
          }
        )

        this.projectMembers.forEach(members => {
          if(members.memberId == clickedMember.id){
            flag = true
          }
        });
        
        if(flag){
          if(!confirm(clickedMember.name + "님 은(는) 이미 다른 팀 또는 현재 팀에 속해 있습니다. 진행할까요?")){
            this.checkedMember.pop();
            e.target.checked = false;
          }
        }
      }
    },
    getMemberList(){
      getAllMembers(
        (res) => {
          let memberList = [];
          res.object.forEach((member) => {
              memberList.push(member);
          })
          this.memberList = memberList;
          console.log(this.memberList);
          this.checkAddOrModify();
        },
        (error) => {
          console.log(error);
        }
      )
    },
    getProjectMembers(){
      getProjectMembers(
        this.projectId,
        (res) => {
          this.projectMembers = res.object;
        },
        (error) => {
          console.log(error);
        }
      )
    },
    onClickOutside() {
      this.closeModal();
    },
    closeModal() {
      this.$emit('closeTeamAddModal');
    },
    typingSearchName(e) {
      this.searchName = e.target.value;
    },
    typingTeamName(e) {
      if (!e.target.value) {
        this.isValid = false;
        return;
      }
      teamNameCheck(
        {
          projectId : this.projectId,
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
    teamAdd() {
      // 팀 이름 입력이 필수!
      if (!this.isValid) {
        return;
      }
      if(!this.isModify){
        createTeam(
          {
            projectId: this.projectId,
            teamName: this.teamName,
            memberList: this.checkedMember
          },
          () => {
            this.closeModal();
          },
          (error) => {
            console.log(error);
            this.closeModal();
          }
        )
      } else {
        modifyTeam(
          {
            teamId: this.selectTeam.teamId,
            memberList: this.checkedMember
          },
          () => {
            this.closeModal();
          },
          (error) => {
            console.log(error);
            this.closeModal();
          }
        )
      }
    },
  },
  computed: {
    ...mapGetters(['projectId']),
    searchByMemberName() {
      return this.memberList.filter((member) => {
        return member.name.includes(this.searchName);
      });
    },
  },
};
</script>
