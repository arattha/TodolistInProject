<template>
  <div class="flex flex-col h-full">
    <div class="flex w-full flex-col lg:flex-row ite lg:justify-between px-10 lg:px-20 my-3">
      <div class="flex flex-col">
        <div class="flex my-3">
          <div class="font-black text-6xl">
            {{ todoInfo.title }}
          </div>
          <div class="flex items-end justify-center text-3xl ml-5">
            <i
              class="fas fa-star text-white cursor-pointer"
              v-if="!userInfo.isBookmark"
              @click="toggleBookmark()"
            ></i>
            <i
              class="fas fa-star text-yellow-400 cursor-pointer"
              v-if="userInfo.isBookmark"
              @click="toggleBookmark()"
            ></i>
          </div>
        </div>
        <div class="my-5">
          <Todo-Status
            class="flex"
            :status="todoInfo.status"
            :isDetail="true"
            @changeStatus="changeStatus"
          />
        </div>
      </div>
      <div class="flex justify-end lg:justify-center lg:items-center mr-5">
        <div class="flex flex-col">
          <div class="flex justify-end lg:justify-start items-center">
            <button
              class="
                bg-itemGray
                text-black text-xs
                font-semibold
                w-16
                h-8
                py-2
                px-2
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
              @click="showTeamMemberMoveModal"
            >
              보내기
            </button>
            <div class="rounded-full w-14 h-14 lg:w-16 lg:h-16 bg-white mr-3">
              <img :src="'http://localhost:8080/img/' + id" />
            </div>
            <div class="flex flex-col">
              <div class="lg:text-2xl">{{ todoInfo.memberName }}</div>
              <div class="text-sm lg:text-base">{{ todoInfo.teamName }}</div>
            </div>
          </div>
          <div class="flex lg:flex-col mt-2 lg:mt-8">
            <div class="flex justify-start lg:justify-end lg:items-center text-sm mr-5 lg:mr-0">
              생성일 : {{ todoInfo.regDate }}
            </div>
            <div class="flex lg:justify-end lg:items-center text-sm">
              변경일 : {{ todoInfo.modifyDate }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="flex flex-col items-center w-full h-full px-10 lg:px-16 overflow-auto">
      <div class="flex justify-end w-full h-12 lg:h-16">
        <div class="flex items-center justify-center w-20 h-12 lg:w-24">
          <button
            class="
              lg:my-auto
              bg-itemGray
              text-black text-sm
              font-semibold
              h-9
              w-20
              py-2
              px-2
              mr-5
              rounded-lg
              shadow-md
              hover:bg-menuGray
              focus:outline-none
              focus:ring-2
              focus:ring-headerGray
              focus:ring-offset-2
              focus:ring-offset-purple-200
            "
            @click="todoContentAdd()"
          >
            추가
          </button>
        </div>
        <div class="flex bg-buttonGray rounded-t-lg w-9/12 lg:w-1/3">
          <div
            class="
              flex
              justify-center
              items-center
              w-1/3
              h-full
              rounded-tl-lg
              hover:bg-bg-itemGray
              cursor-pointer
              font-black
              border border-b-0 border-menuGray
            "
            @click="goDetail()"
            :class="{
              'bg-itemGray': curPage === 0,
            }"
          >
            상세내용
          </div>
          <div
            class="
              flex
              justify-center
              items-center
              w-1/3
              hover:bg-itemGray
              cursor-pointer
              font-black
              border-t border-menuGray
            "
            @click="goURL()"
            :class="{
              'bg-itemGray': curPage === 1,
            }"
          >
            URL
          </div>
          <div
            class="
              flex
              justify-center
              items-center
              w-1/3
              rounded-tr-lg
              hover:bg-itemGray
              cursor-pointer
              font-black
              border border-b-0 border-menuGray
            "
            @click="goHistory()"
            :class="{
              'bg-itemGray': curPage === 2,
            }"
          >
            히스토리
          </div>
        </div>
      </div>
      <router-view
        class="
          flex flex-col
          items-center
          bg-itemGray
          overflow-auto
          scroll_type2
          w-full
          h-full
          pt-3
          mb-5
          border-t border-buttonGray
          rounded-tl-lg rounded-b-lg
          shadow-lg
        "
      >
      </router-view>
    </div>
    <Todo-Detail-Modal v-if="isShow" @closeModal="closeModal" :todoId="todoId" :memberId="id" />
    <Todo-Team-Member-Move-Modal
      v-if="isTeamMemberMoveModalShow"
      @closeModal="closeTeamMemberMoveModal"
      :todoId="todoInfo.id"
    />
  </div>
</template>

<script>
import TodoStatus from '@/components/TodoStatus.vue';
import { mapGetters, mapActions } from 'vuex';
import TodoDetailModal from '@/components/modal/TodoDetailModal.vue';
import TodoTeamMemberMoveModal from '@/components/modal/TodoTeamMemberMoveModal.vue';
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";

export default {
  name: 'TODODETAIL',
  components: {
    TodoStatus,
    TodoDetailModal,
    TodoTeamMemberMoveModal,
  },
  data() {
    return {
      isTeamMemberMoveModalShow: false,
      isShow: false,
      curPage: 0,
      todoInfo: {
        id: '',
        title: '',
        memberId: '',
        memberName: '',
        teamName: '',
        status: '',
        regDate: '',
        modifyDate: '',
      },
      userInfo: {
        name: '',
        teamName: '',
        // 즐겨찾기 여부
        isBookmark: true,
      },
    };
  },
  computed: {
    ...mapGetters(['id', 'todoId', 'stomp']),
  },
  created() {
    this.curPage = 0;
    this.connect();
  },
  methods: {
    ...mapActions(['toggle_reload_todo_detail', 'set_totalAlarmCnt', 'set_stomp']),
    connect() {
      console.log("todoid ", this.todoId);
      console.log("stomp", this.stomp);
      this.stomp.send(
        '/server/getTodoInfo',
        JSON.stringify({
          todoId: this.todoId,
        }),
        {}
      );

      this.stomp.subscribe('/client/detail/' + this.todoId, (res) => {
        var todo = JSON.parse(res.body);
        console.log("todo :",todo);
        this.todoInfo.id = todo.id;
        this.todoInfo.title = todo.title;
        this.todoInfo.memberId = todo.memberId;
        this.todoInfo.memberName = todo.memberName;
        this.todoInfo.teamName = todo.teamName;
        this.todoInfo.status = todo.status;
        this.todoInfo.modifyDate = todo.modifyDate.split('T')[0];
        this.todoInfo.regDate = todo.regDate.split('T')[0];
      });
      
    },
    changeStatus(status) {
      this.todoInfo.status = status;
      console.log("todoinfo in detail :",this.todoInfo);
      this.stomp.send('/server/movoTodo/status',
      JSON.stringify({
        id: this.todoInfo.id,
        title: this.todoInfo.title,
        status: this.todoInfo.status,
        projectId: this.todoInfo.projectId,
        teamId: this.todoInfo.teamId,
        teamName: this.todoInfo.teamName,
        memberId: this.todoInfo.id,
        memberName: this.todoInfo.name,
        modifyDate: this.todoInfo.modifyDate,
        regDate: this.todoInfo.regDate,
      }),
      {});
    },
    todoContentAdd() {
      this.showModal();
    },
    toggleBookmark() {
      this.userInfo.isBookmark = !this.userInfo.isBookmark;
    },
    goDetail() {
      this.curPage = 0;
      if (this.$route.path !== `/${this.$route.params.todoId}/detail`) {
        this.$router.push(`/${this.$route.params.todoId}/detail`);
      }
    },
    goURL() {
      this.curPage = 1;
      if (this.$route.path !== `/${this.$route.params.todoId}/detail/url`) {
        this.$router.push(`/${this.$route.params.todoId}/detail/url`);
      }
    },
    goHistory() {
      this.curPage = 2;
      if (this.$route.path !== `/${this.$route.params.todoId}/detail/history`) {
        this.$router.push(`/${this.$route.params.todoId}/detail/history`);
      }
    },
    showModal() {
      this.isShow = true;
    },
    closeModal(val) {
      this.isShow = false;

      if (val) {
        return;
      }

      if (this.curPage !== 0) {
        this.goDetail();
      } else {
        this.toggle_reload_todo_detail(true);
      }
    },
    showTeamMemberMoveModal() {
      this.isTeamMemberMoveModalShow = true;
    },
    closeTeamMemberMoveModal() {
      this.isTeamMemberMoveModalShow = false;
    },
  },
  beforeRouteLeave(to, from, next) {
    // just use `this` this.name = to.params.name next()
    if (to.fullPath !== from.fullPath) {
      this.stomp.disconnect();
      const serverURL = 'http://localhost:8082/socket';
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket, { debug: false });
      this.stompClient.connect({}, () => {

        this.set_stomp(this.stompClient);
        // 소켓 연결 성공
        this.connected = true;
        this.stompClient.debug = () => {};
        this.stompClient.send(
          '/server/getAlarm',
          JSON.stringify({
            memberId: this.id,
          }),
          {}
        );

        this.stompClient.subscribe('/client/alarm/' + this.id, (res) => {
          this.alarmList = JSON.parse(res.body);
          this.set_totalAlarmCnt(this.alarmList.length);
        });
      });
    }

    next();
  },
};
</script>
