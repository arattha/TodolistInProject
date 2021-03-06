<template>
  <div class="flex flex-col h-full w-full mt-4">
    <div class="flex mb-5 px-8 w-full h-10 mt-1">
      <select
        class="
          flex
          bg-contentGray
          border border-transparent
          w-64
          py-2
          px-4
          text-gray-700
          placeholder-gray-400
          shadow-md
          rounded-lg
          mr-8
          focus:outline-none focus:ring-1 focus:ring-headerGray focus:border-transparent
        "
        v-model="selectTeam"
        @change="changeTeam"
      >
        <option v-for="(team, index) in teamList" :key="index" :value="team">
          {{ team.teamName }}
        </option>
      </select>
      <button
        class="
          bg-itemGray
          text-black
          lg:text-base
          text-sm
          font-semibold
          h-full
          w-20
          lg:w-24 lg:py-2 lg:px-2
          mr-8
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        @click="showTeamNameModal()"
      >
        이름 수정
      </button>
      <button
        class="
          bg-itemGray
          text-black
          lg:text-base
          text-sm
          font-semibold
          h-full
          w-20
          lg:w-24 lg:py-2 lg:px-2
          mr-8
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        @click="showTeamAddModal()"
      >
        팀 수정
      </button>
      <button
        class="
          bg-itemGray
          text-black
          lg:text-base
          text-sm
          font-semibold
          w-20
          lg:w-24 lg:py-2 lg:px-4
          mr-8
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        @click="todoFilter()"
      >
        필터
      </button>
      <button
        class="
          text-black
          lg:text-base
          text-sm
          font-semibold
          w-20
          lg:w-24 lg:py-2 lg:px-4
          rounded-lg
          shadow-md
          hover:bg-menuGray
          focus:outline-none
          focus:ring-2
          focus:ring-headerGray
          focus:ring-offset-2
          focus:ring-offset-purple-200
        "
        :class="[bookmarkFilter ? 'bg-menuGray' : 'bg-itemGray']"
        @click="activeBookmarkFilter()"
      >
        즐겨찾기
      </button>
    </div>
    <div id="scroll_div" class="flex overflow-x-auto px-8 mb-1 scroll_type1 h-full pb-2 w-full">
      <Status-Kanban
        v-for="(statusInfo, index) in statusFilter"
        :key="index"
        :status="statusInfo.status"
        :todoList="statusInfo.todoList"
        :bookmarkFilter="bookmarkFilter"
        :selectedTeam="selectTeam"
        @changeStatus="changeStatus"
        :stomp="stomp"
      />
    </div>
    <TeamUpdateModal
      v-if="isTeamNameShow"
      @closeTeamNameModal="closeTeamNameModal"
      :teamId="selectTeam.teamId"
    />
    <MyTodoFilter
      v-if="isShow"
      @closeModal="closeFilter"
      @cleanFilter="cleanFilter"
      @applyFilter="applyFilter"
    />
    <Team-Add-Modal
      v-if="isShowTeamAddModal"
      :selectTeam="selectTeam"
      :modifyMemberList="teamMemberList"
      @closeTeamAddModal="closeTeamAddModal"
    />
  </div>
</template>

<script>
import StatusKanban from '@/components/kanban/StatusKanban.vue';
import TeamUpdateModal from '@/components/modal/TeamUpdateModal.vue';
import TeamAddModal from '@/components/modal/TeamAddModal.vue';
import MyTodoFilter from '@/components/MyTodoFilter.vue';
import { getBookmark } from '@/api/bookmark.js';
import { getMembersByTeam } from '@/api/auth.js';
import { mapGetters, mapActions } from 'vuex';
import { getTeam } from '@/api/team.js';

export default {
  name: 'TEAMTODO',
  components: {
    StatusKanban,
    TeamUpdateModal,
    MyTodoFilter,
    TeamAddModal,
  },
  props: ['stomp'],
  data() {
    return {
      teamList: [],
      selectTeam: '',
      statusInfoList: [
        {
          status: 'New',
          todoList: [],
        },
        {
          status: '접수',
          todoList: [],
        },
        {
          status: '진행',
          todoList: [],
        },
        {
          status: '완료',
          todoList: [],
        },
        {
          status: '진행하지않음',
          todoList: [],
        },
      ],
      teamMemberList: [],
      todoInfoList: [],
      bookmarkFilter: false,
      isTeamNameShow: false,
      filters: null,
      isShow: false,
      isShowTeamAddModal: false,
    };
  },
  async created() {
    await this.getTeamList();
  },
  computed: {
    ...mapGetters(['projectId', 'id', 'bookmarkList']),
    statusFilter: function () {
      let filters = this.filters;
      if (filters == null) {
        return this.statusInfoList; //filter가 없을 때는 원본 반환
      } else {
        return this.statusInfoList.filter(function (status) {
          if (filters.status.indexOf(status.status) > -1) {
            return true;
          }
        });
      }
    },
  },
  methods: {
    ...mapActions(['set_bookmarkList', 'push_bookmarkList']),
    getTeamList() {
      getTeam(
        this.projectId,
        (res) => {
          // team 가져옴
          this.teamList = [];

          res.object.forEach((value) => {
            this.teamList.push({
              teamId: value.id,
              teamName: value.name,
            });
          });

          this.getBookmarkList();
          const findTeam = this.teamList.find((val) => {
            if (val.teamId === this.$route.params.teamId) {
              return true;
            }
          });

          this.selectTeam = findTeam;
          this.getModifyMemberList();
          this.setStatusInfoList();
        },
        () => {
          console.log('team 가져오기 실패');
        }
      );
    },
    setStatusInfoList() {
      this.stomp.send(
        '/server/getTeamTodo',
        JSON.stringify({
          projectId: this.projectId,
          teamId: this.selectTeam.teamId,
        }),
        {}
      );

      // subscribe 로 alarm List 가져오기
      this.stomp.subscribe(
        '/client/todo/' + this.projectId + '/team/' + this.selectTeam.teamId,
        (res) => {
          this.statusInfoList = JSON.parse(res.body);
          this.updateList();
        }
      );
    },
    getModifyMemberList() {
      getMembersByTeam(
        this.selectTeam.teamId,
        (res) => {
          this.teamMemberList = res.object;
        },
        (error) => {
          console.log(error);
        }
      );
    },
    changeTeam() {
      // stautsInfoList를 초기화
      this.setStatusInfoList();
    },
    setStatusTodo() {
      for (let i = 0; i < this.todoInfoList.length; ++i) {
        if (this.todoInfoList[i].status === 'New') {
          this.statusInfoList[0].todoList.push(this.todoInfoList[i]);
        } else if (this.todoInfoList[i].status === '접수') {
          this.statusInfoList[1].todoList.push(this.todoInfoList[i]);
        } else if (this.todoInfoList[i].status === '진행') {
          this.statusInfoList[2].todoList.push(this.todoInfoList[i]);
        } else if (this.todoInfoList[i].status === '완료') {
          this.statusInfoList[3].todoList.push(this.todoInfoList[i]);
        } else {
          this.statusInfoList[4].todoList.push(this.todoInfoList[i]);
        }
      }
    },
    changeStatus(val) {
      if (val.status === 'New') {
        this.statusInfoList[0].todoList[val.index].status = val.status;
      } else if (val.status === '접수') {
        this.statusInfoList[1].todoList[val.index].status = val.status;
      } else if (val.status === '진행') {
        this.statusInfoList[2].todoList[val.index].status = val.status;
      } else if (val.status === '완료') {
        this.statusInfoList[3].todoList[val.index].status = val.status;
      } else {
        this.statusInfoList[4].todoList[val.index].status = val.status;
      }

      // this.stomp.send(
      //   '/server/moveTodo/status',
      //   JSON.stringify({
      //     projectId: this.projectId,
      //     teamId: this.selectTeam.id,
      //   }),
      //   {}
      // );
    },
    async getBookmarkList() {
      await getBookmark(
        {
          projectId: this.projectId,
          memberId: this.id,
        },
        (res) => {
          let tmp = [];
          res.object.forEach((bookmark) => {
            tmp.push(bookmark.todoId);
          });
          this.set_bookmarkList(tmp);
        },
        (error) => {
          if (this.isLogin) {
            alert('즐겨찾기 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
            console.log(error);
          }
        }
      );
    },
    updateList() {
      for (var i = 0; i < this.statusInfoList.length; i++) {
        for (var j = 0; j < this.statusInfoList[i].todoList.length; j++) {
          if (this.bookmarkList.indexOf(this.statusInfoList[i].todoList[j].id) > -1) {
            this.statusInfoList[i].todoList[j].bookmark = true;
          } else {
            this.statusInfoList[i].todoList[j].bookmark = false;
          }
        }
      }
    },
    activeBookmarkFilter() {
      this.bookmarkFilter = !this.bookmarkFilter;
    },
    showTeamNameModal() {
      this.isTeamNameShow = true;
    },
    closeTeamNameModal() {
      this.isTeamNameShow = false;
    },
    todoFilter() {
      this.isShow = true;
    },
    closeFilter() {
      this.stomp.send(
        '/server/getTeamTodo',
        JSON.stringify({
          projectId: this.projectId,
          teamId: this.selectTeam.teamId,
        }),
        {}
      );
      this.isShow = false;
    },
    applyFilter(filters) {
      this.filters = filters;
      this.isShow = false;
    },
    cleanFilter() {
      this.filters = null;
      this.isShow = false;
    },
    showTeamAddModal() {
      this.isShowTeamAddModal = true;
    },
    closeTeamAddModal() {
      this.isShowTeamAddModal = false;
    },
  },
};
</script>
