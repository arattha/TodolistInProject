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
                h-9
                w-24
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
              @click="modifyManager()"
            >
              담당자 변경
            </button>
            <div class="rounded-full w-14 h-14 lg:w-16 lg:h-16 bg-white mr-3">
              <img :src="'http://localhost:8080/img/' + todoInfo.memberId" />
            </div>
            <div class="flex flex-col">
              <div class="lg:text-2xl">OOO</div>
              <div class="text-sm lg:text-base">XXX</div>
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
    <Todo-Detail-Modal v-if="isShow" @closeModal="closeModal" />
  </div>
</template>

<script>
import TodoStatus from '@/components/TodoStatus.vue';
import { mapGetters } from 'vuex';
import TodoDetailModal from '@/components/modal/TodoDetailModal.vue';

export default {
  name: 'TODODETAIL',
  components: {
    TodoStatus,
    TodoDetailModal,
  },
  data() {
    return {
      isShow: false,
      curPage: 0,
      todoInfo: {
        id: '1',
        title: '회원가입',
        memberId: '0564293048818',
        status: '접수',
        regDate: '2021-10-14',
        modifyDate: '2021-10-15',
      },
      userInfo: {
        name: '최광진',
        teamName: 'F106',
        // 즐겨찾기 여부
        isBookmark: true,
      },
    };
  },
  computed: {
    ...mapGetters(['id', 'todoId']),
  },
  created() {
    if (this.$route.path === '/todo/detail') {
      this.curPage = 0;
    }
  },
  methods: {
    changeStatus(status) {
      this.todoInfo.status = status;
    },
    modifyManager() {},
    todoContentAdd() {
      this.showModal();
    },
    toggleBookmark() {
      this.userInfo.isBookmark = !this.userInfo.isBookmark;
    },
    goDetail() {
      this.curPage = 0;
      if (this.$route.path !== `/${this.$routes.todoId}/detail`) {
        this.$router.push(`/${this.$routes.todoId}/detail`);
      }
    },
    goURL() {
      this.curPage = 1;
      if (this.$route.path !== `/${this.$routes.todoId}/detail/url`) {
        this.$router.push(`/${this.$routes.todoId}/detail/url`);
      }
    },
    goHistory() {
      this.curPage = 2;
      if (this.$route.path !== `/${this.$routes.todoId}/detail/history`) {
        this.$router.push(`/${this.$routes.todoId}/detail/history`);
      }
    },
    showModal() {
      this.isShow = true;
    },
    closeModal() {
      this.isShow = false;
    },
  },
};
</script>
