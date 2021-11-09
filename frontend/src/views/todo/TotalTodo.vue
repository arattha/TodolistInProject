<template>
  <div class="flex flex-col h-full w-full">
    <div class="flex my-5 px-8 w-full h-10">
      <button
        class="
          bg-itemGray
          text-black
          lg:text-base
          text-sm
          font-semibold
          h-full
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
        @click="teamAdd()"
      >
        팀 추가
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
        :class="[bookmarkFilter ? 'bg-itemGray' : 'bg-menuGray']"
        @click="activeBookmarkFilter()"
      >
        즐겨찾기
      </button>
    </div>

    <div id="scroll_div" class="flex overflow-x-auto px-8 mb-1 scroll_type1 h-full">
      <div class="flex pb-3 mr-8" v-for="(teamInfo, index) in teamFilter" :key="index">
        <Total-Kanban :teamInfo="teamInfo" :filters="filters" :TodoStomp="stompClient" :bookmarkFilter="bookmarkFilter" />
      </div>
    </div>
    <TodoFilter
      v-if="isShow"
      @closeModal="closeModal"
      @cleanFilter="cleanFilter"
      @applyFilter="applyFilter"
      :teamInfoList="teamInfoList"
    />
    <Team-Add-Modal v-if="isShowTeamAddModal" @closeTeamAddModal="closeTeamAddModal" />
  </div>
</template>

<script>
import TotalKanban from '@/components/TotalKanban.vue';
import TodoFilter from '@/components/TodoFilter.vue';
import TeamAddModal from '@/components/modal/TeamAddModal.vue';
import { mapGetters, mapActions } from 'vuex';
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { getTeam } from '@/api/team.js';
import { getBookmark } from '@/api/bookmark.js';

export default {
  name: 'PJTTODO',
  components: {
    TotalKanban,
    TodoFilter,
    TeamAddModal,
  },
  data() {
    return {
      teamInfoList: [],
      todoList: [],
      teamList: [],
      isShow: false,
      filters: null,
      bookmarkFilter: false,
      isShowTeamAddModal: false,
    };
  },

  created() {
    this.isShow = false;
    this.set_project_id(this.projectId);
    this.connect();
    this.set_project_name(this.projectName);
  },
  computed: {
    ...mapGetters(['projectId','id','projectName']),
    teamFilter: function () {
      let filters = this.filters;
      if (filters == null) {
        return this.teamInfoList; //filter가 없을 때는 원본 반환
      } else {
        return this.teamInfoList.filter(function (team) {
          if (filters.team.indexOf(team.teamName) > -1) {
            return true;
          }
        });
      }
    },
  },
  methods: {
    ...mapActions(['set_project_name', 'set_project_id']),
    connect() {
      const serverURL = 'http://localhost:8082/todo';
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket, { debug: false });
      this.stompClient.connect(
        {},
        () => {
          // 소켓 연결 성공
          this.connected = true;

          this.stompClient.debug = () => {};

          this.stompClient.send(
            '/server/getTodo',
            JSON.stringify({
              projectId: this.projectId,
            }),
            {}
          );

          // subscribe 로 alarm List 가져오기
          this.stompClient.subscribe('/client/todo/' + this.projectId, (res) => {
            this.todoList = JSON.parse(res.body);
            this.getTeamList();
          });
        },
        (error) => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
        }
      );
    },
    getTeamList() {
      getTeam(
        this.projectId,
        (res) => {
          // team 가져옴

          var tmp = res.object;

          this.teamInfoList = [];

          tmp.forEach((value) => {
            this.teamInfoList.push({
              teamId: value.id,
              teamName: value.name,
              totalCnt: 330,
              addCnt: 30,
              doneCnt: 30,
              progressCnt: 20,
              todoInfoList: [],
            });
          });
          this.updateList();
        },
        () => {
          console.log('team 가져오기 실패');
        }
      );
    },
    updateList() {
      for (var i = 0; i < this.todoList.length; i++) {
        var teamId = this.todoList[i].teamId;

        for (var j = 0; j < this.teamInfoList.length; j++) {
          if (this.teamInfoList[j].teamId == teamId) {
            this.teamInfoList[j].todoInfoList.push({
              id: this.todoList[i].id,
              title: this.todoList[i].title,
              status: this.todoList[i].status,
              projectId: this.todoList[i].projectId,
              teamId: teamId,
              memberId: this.todoList[i].memberId,
              memberName: this.todoList[i].memberName,
              modifyDate: this.todoList[i].modifyDate,
              regDate: this.todoList[i].regDate,
              isBookmark:false,
            });

            break;
          }
        }
      }
      this.getBookmarkList();
    },
    async getBookmarkList(){
      let tmp = [];
      await getBookmark(
        {
          projectId : this.projectId,
          memberId : this.id
        },
        (res) => {
          res.object.forEach(bookmark => {
            tmp.push(bookmark.todoId);
          });
        },
        (error) => {
          alert('즐겨찾기 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
          console.log(error);
        }
      );
      for (var i = 0; i < this.teamInfoList.length; i++) {
        for (var j = 0; j < this.teamInfoList[i].todoInfoList.length; j++) {
          if (tmp.indexOf(this.teamInfoList[i].todoInfoList[j].id) > -1 ) {
            this.teamInfoList[i].todoInfoList[j].isBookmark = true;
          }
        }
      }

    },
    activeBookmarkFilter(){
      this.bookmarkFilter = !this.bookmarkFilter;
    },
    horizontalScroll() {
      console.log('hi', this);
    },
    onWheel(e) {
      let item = document.getElementById('scroll_div');

      if (e.deltaY > 0) item.scrollLeft += 100;
      else item.scrollLeft -= 100;
    },
    teamAdd() {
      this.showTeamAddModal();
    },
    todoFilter() {
      this.isShow = true;
    },
    closeModal() {
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
