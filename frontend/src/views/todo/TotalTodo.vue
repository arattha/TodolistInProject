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
        :class="[bookmarkFilter ? 'bg-menuGray' : 'bg-itemGray']"
        @click="activeBookmarkFilter()"
      >
        즐겨찾기
      </button>
    </div>

    <div id="scroll_div" class="flex overflow-x-auto px-8 mb-1 scroll_type1 h-full">
      <div class="flex pb-3 mr-8" v-for="(teamInfo, index) in teamFilter" :key="index">
        <Total-Kanban :teamInfo="teamInfo" :filters="filters" :bookmarkFilter="bookmarkFilter" />
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
      bookmarkList:[],
      todoList: [],
      teamList: [],
      isShow: false,
      filters: null,
      bookmarkFilter: false,
      isShowTeamAddModal: false,
    };
  },

  async created() {
    await this.getBookmarkList();
    this.isShow = false;
    this.set_project_id(this.projectId);
    this.connect();
    this.set_project_name(this.projectName);
  },
  computed: {
    ...mapGetters(['projectId', 'id', 'projectName', 'stomp', 'isFirst']),
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
    ...mapActions(['set_project_name', 'set_project_id', 'set_stomp']),
    async connect() {
        this.stomp.send(
          '/server/getTodo',
          JSON.stringify({
            projectId: this.projectId,
          }),
          {}
        );

        // subscribe 로 alarm List 가져오기
        await this.stomp.subscribe('/client/todo/' + this.projectId, (res) => {
          this.teamInfoList = JSON.parse(res.body);
        });
        this.updateList();

    },
    updateList() {
      for (var i = 0; i < this.teamInfoList.length; i++) {
        for (var j = 0; j < this.teamInfoList[i].length; j++) {

          if(this.bookmarkList.indexOf(this.teamInfoList[i].todoInfoList[j].id) > -1){
            this.teamInfoList[i].todoInfoList[j].isBookmark = true;
          } else {
            this.teamInfoList[i].todoInfoList[j].isBookmark = false;
          }
        }
      }
    },
    async getBookmarkList() {
      
      this.bookmarkList = [];
      await getBookmark(
        {
          projectId: this.projectId,
          memberId: this.id,
        },
        (res) => {
          res.object.forEach((bookmark) => {
            this.bookmarkList.push(bookmark.todoId);
          });
        },
        (error) => {
          alert('즐겨찾기 목록 받아오는데 문제가 발생했습니다. 새로고침 해주세요!!');
          console.log(error);
        }
      );
      
    },
    activeBookmarkFilter() {
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
