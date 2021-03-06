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
        @click="addMyTodo()"
      >
        할일 추가
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
      <Status-Kanban
        v-for="(statusInfo, index) in statusFilter"
        :key="index"
        :status="statusInfo.status"
        :todoList="statusInfo.todoList"
        :bookmarkFilter="bookmarkFilter"
        @changeStatus="changeStatus"
        :stomp="stomp"
      />
    </div>
    <MyTodoFilter
      v-if="isShow"
      @closeModal="closeFilter"
      @cleanFilter="cleanFilter"
      @applyFilter="applyFilter"
    />
    <My-Todo-Add-Modal v-if="isModalShow" @closeModal="closeModal" :stomp="stomp" />
  </div>
</template>

<script>
import StatusKanban from '@/components/kanban/StatusKanban.vue';
import MyTodoFilter from '@/components/MyTodoFilter.vue';
import MyTodoAddModal from '@/components/modal/MyTodoAddModal.vue';
import { getBookmark } from '@/api/bookmark.js';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'MYTODO',
  components: {
    StatusKanban,
    MyTodoFilter,
    MyTodoAddModal,
  },
  props: ['stomp'],
  data() {
    return {
      statusInfoList: [
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
      todoInfoList: [],
      bookmarkFilter: false,
      filters: null,
      isShow: false,
      isModalShow: false,
    };
  },
  created() {
    this.getBookmarkList();
  },
  computed: {
    ...mapGetters(['projectId', 'id', 'projectName', 'bookmarkList']),
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
    ...mapActions(['set_totalAlarmCnt', 'set_bookmarkList']),
    connect() {
      this.stomp.send(
        '/server/getMyTodo',
        JSON.stringify({
          projectId: this.projectId,
          memberId: this.id,
        }),
        {}
      );

      // subscribe 로 alarm List 가져오기
      this.stomp.subscribe('/client/todo/' + this.projectId + '/' + this.id, (res) => {
        this.statusInfoList = JSON.parse(res.body).filter((element) => element.status !== 'New');
        this.updateList();
      });
    },
    addMyTodo() {
      this.showModal();
    },
    showModal() {
      this.isModalShow = true;
    },
    closeModal() {
      this.isModalShow = false;
    },
    todoFilter() {
      this.isShow = true;
    },
    closeFilter() {
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
    changeStatus(val) {
      if (val.status === '접수') {
        this.statusInfoList[1].todoList[val.index].status = val.status;
      } else if (val.status === '진행') {
        this.statusInfoList[2].todoList[val.index].status = val.status;
      } else if (val.status === '완료') {
        this.statusInfoList[3].todoList[val.index].status = val.status;
      } else {
        this.statusInfoList[4].todoList[val.index].status = val.status;
      }

      this.stomp.send('server/moveTodo/status', this.statusInfoList[0].todoList[val.index], {});
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
          this.connect();
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
  },
};
</script>
