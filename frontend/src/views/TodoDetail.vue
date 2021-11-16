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
              v-if="!isBookmark"
              @click="bookmark()"
            ></i>
            <i
              class="fas fa-star text-yellow-400 cursor-pointer"
              v-if="isBookmark"
              @click="bookmark()"
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
            <ul>
              <li>
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
                    mt-4
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
              </li>
              <li>
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
                    mt-4
                    rounded-lg
                    shadow-md
                    hover:bg-menuGray
                    focus:outline-none
                    focus:ring-2
                    focus:ring-headerGray
                    focus:ring-offset-2
                    focus:ring-offset-purple-200
                  "
                  @click="showUpdateTodoModal"
                >
                  수정하기
                </button>
              </li>
            </ul>

            <div class="rounded-full w-14 h-14 lg:w-16 lg:h-16 bg-white mr-3">
              <img :src="'http://localhost:8080/img/' + todoInfo.memberId" />
            </div>
            <div class="flex flex-col">
              <div class="lg:text-2xl">{{ todoInfo.memberName }}</div>
              <div class="text-sm lg:text-base">{{ todoInfo.teamName }}</div>
            </div>
          </div>
          <div class="flex lg:flex-col mt-2 lg:mt-8">
            <div class="flex justify-start lg:justify-end lg:items-center text-sm mr-5 lg:mr-0">
              생성일 : {{ todoInfo.regDate.split('T')[0] }}
            </div>
            <div class="flex lg:justify-end lg:items-center text-sm">
              변경일 : {{ todoInfo.modifyDate.split('T')[0] }}
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
    <Todo-Detail-Modal
      v-if="isShow"
      @closeModal="closeModal"
      :todoId="todoId"
      :memberId="id"
      :stomp="stompClient"
    />
    <Todo-Team-Member-Move-Modal
      v-if="isTeamMemberMoveModalShow"
      @closeModal="closeTeamMemberMoveModal"
      @justCloseModal="justCloseModal" 
      :todoInfo="todoInfo"
      :isDetail="true"
      :stomp="stompClient"
    />
    <Todo-Update-Modal
      v-if="isUpdateTodoModalShow"
      @closeModal="closeUpdateTodoModal"
      :todoInfo="todoInfo"
      :stomp="stompClient"
    ></Todo-Update-Modal>
  </div>
</template>

<script>
import TodoStatus from '@/components/TodoStatus.vue';
import { mapGetters, mapActions } from 'vuex';
import TodoDetailModal from '@/components/modal/TodoDetailModal.vue';
import TodoTeamMemberMoveModal from '@/components/modal/TodoTeamMemberMoveModal.vue';
import TodoUpdateModal from '@/components/modal/TodoUpdateModal.vue';
import { addBookmark, deleteBookmark } from '@/api/bookmark.js';
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';

export default {
  name: 'TODODETAIL',
  components: {
    TodoStatus,
    TodoDetailModal,
    TodoTeamMemberMoveModal,
    TodoUpdateModal,
  },
  data() {
    return {
      isTeamMemberMoveModalShow: false,
      isUpdateTodoModalShow: false,
      isShow: false,
      curPage: 0,
      todoInfo: {
        id: '',
        title: '',
        memberId: null,
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
      },
      isBookmark: false,
    };
  },
  computed: {
    ...mapGetters(['id', 'todoId', 'bookmarkList']),
  },
  created() {
    this.curPage = 0;
    this.connect();
  },
  methods: {
    ...mapActions([
      'toggle_reload_todo_detail',
      'set_totalAlarmCnt',
      'push_bookmarkList',
      'delete_bookmark',
    ]),
    connect() {
      const serverURL = 'http://localhost:8082/socket';
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket, { debug: false });
      this.stompClient.connect({}, () => {
        // 소켓 연결 성공
        this.connected = true;
        this.stompClient.debug = () => {};
        this.stompClient.send(
          '/server/getTodoInfo',
          JSON.stringify({
            todoId: this.todoId,
          }),
          {}
        );

        this.stompClient.subscribe('/client/detail/' + this.todoId, (res) => {
          this.todoInfo = JSON.parse(res.body);
        });
      });

      if (this.bookmarkList.indexOf(this.todoId) > -1) {
        this.isBookmark = true;
      }
    },
    changeStatus(status) {
      
      if(this.todoInfo.status == "New"){
        // 전 상태가 New 였다면 보내기 모달창 활성화
        this.isTeamMemberMoveModalShow = true;
      } else if(status == "New"){
        // 바꿀 상태가 New라면 담당자를 없음으로 변경
        this.todoInfo.status = status;
        this.todoInfo.memberId = null;
        this.stompClient.send('/server/moveTodo/status', JSON.stringify(this.todoInfo), {});
      } else {
        this.todoInfo.status = status;
        this.stompClient.send('/server/moveTodo/status', JSON.stringify(this.todoInfo), {});
      }

    },
    todoContentAdd() {
      this.showModal();
    },
    bookmark() {
      if (!this.isBookmark) {
        addBookmark(
          {
            memberId: this.id,
            todoId: this.todoId,
          },
          () => {
            this.push_bookmarkList(this.todoId);
            this.isBookmark = true;
          },
          (error) => {
            if (this.isLogin) {
              alert('북마크 실패');
              console.log(error);
            }
          }
        );
      } else {
        deleteBookmark(
          {
            memberId: this.id,
            todoId: this.todoId,
          },
          () => {
            this.delete_bookmark(this.todoId);
            this.isBookmark = false;
          },
          (error) => {
            if (this.isLogin) {
              alert('북마크 실패');
              console.log(error);
            }
          }
        );
      }
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
    justCloseModal() {
      this.isTeamMemberMoveModalShow = false;
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
    showUpdateTodoModal() {
      this.isUpdateTodoModalShow = true;
    },
    closeUpdateTodoModal() {
      this.isUpdateTodoModalShow = false;
    },
  },
};
</script>
