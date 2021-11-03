<template>
  <div class="flex flex-col h-full">
    <Header />
    <Header-Todo-Menu :type="'total'" />
    <div class="flex overflow-auto flex-col">
      <div class="flex my-5 mx-8 w-56">
        <button
          class="
            bg-itemGray
            text-black text-base
            font-semibold
            h-full
            w-24
            py-2
            px-4
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
            text-black text-base
            font-semibold
            w-24
            py-2
            px-4
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
      </div>

      <div id="scroll_div" class="flex overflow-x-auto px-8 mb-1 scroll_type1">
        <div class="flex pb-3 mr-8" v-for="(teamInfo, index) in teamInfoList" :key="index">
          <Total-Kanban :teamInfo="teamInfo" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import HeaderTodoMenu from '@/components/HeaderTodoMenu.vue';
import TotalKanban from '@/components/TotalKanban.vue';
import { mapActions } from 'vuex';

export default {
  name: 'PJTTODO',
  components: {
    Header,
    TotalKanban,
    HeaderTodoMenu,
  },
  data() {
    return {
      teamInfoList: [
        {
          teamName: 'OO팀',
          totalCnt: 110,
          addCnt: 10,
          doneCnt: 50,
          progressCnt: 50,
          todoInfoList: [
            {
              status: '진행',
              name: '조용일',
              todoName: '로그인',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },

            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },

            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
          ],
        },
        {
          teamName: 'XX팀',
          totalCnt: 220,
          addCnt: 20,
          doneCnt: 20,
          progressCnt: 20,
          todoInfoList: [
            {
              status: 'New',
              name: '조용일',
              todoName: '로그인',
            },
            {
              status: '완료',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
          ],
        },
        {
          teamName: 'YY팀',
          totalCnt: 330,
          addCnt: 30,
          doneCnt: 30,
          progressCnt: 20,
          todoInfoList: [
            {
              status: '진행',
              name: '조용일',
              todoName: '로그인',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
            {
              status: '접수',
              name: '최광진',
              todoName: '회원가입',
            },
          ],
        },
        {
          teamName: 'AA팀',
          totalCnt: 330,
          addCnt: 30,
          doneCnt: 30,
          progressCnt: 20,
          todoInfoList: [
            {
              status: '진행',
              name: '조용일',
              todoName: '로그인',
            },
          ],
        },
        {
          teamName: 'BB팀',
          totalCnt: 330,
          addCnt: 30,
          doneCnt: 30,
          progressCnt: 20,
          todoInfoList: [
            {
              status: '진행',
              name: '조용일',
              todoName: '로그인',
            },
          ],
        },
        {
          teamName: 'CC팀',
          totalCnt: 330,
          addCnt: 30,
          doneCnt: 30,
          progressCnt: 20,
          todoInfoList: [],
        },
      ],
    };
  },

  created() {
    this.set_project_name('프로젝트 명');
  },
  methods: {
    ...mapActions(['set_project_name']),
    horizontalScroll() {
      console.log('hi', this);
    },
    onWheel(e) {
      let item = document.getElementById('scroll_div');

      if (e.deltaY > 0) item.scrollLeft += 100;
      else item.scrollLeft -= 100;
    },
    teamAdd() {
      console.log('팀추가');
    },
    todoFilter() {
      console.log('할일 필터');
    },
  },
};
</script>
