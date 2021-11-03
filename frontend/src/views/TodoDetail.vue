<template>
  <div class="flex flex-col h-full">
    <Header />
    <Header-Todo-Menu />
    <div class="flex w-full flex-col lg:flex-row ite lg:justify-between px-10 lg:px-20">
      <div class="flex flex-col">
        <div class="flex my-8">
          <div class="font-black text-6xl">
            {{ todoInfo.todoName }}
          </div>
          <div class="flex items-end justify-center">
            <i class="fas fa-star text-white"></i>
            <i class="fas fa-star text-yellow-400"></i>
          </div>
        </div>
        <div class="mb-5">
          <Todo-Status class="flex" :status="'진행'" :isDetail="true" />
        </div>
        <div class="flex justify-between">
          <div>생성일 : {{ todoInfo.regDate }}</div>
          <div>변경일 : {{ todoInfo.modifyDate }}</div>
        </div>
      </div>
      <div class="flex justify-end lg:justify-start items-center my-8 mr-5 h-16">
        <div class="flex items-center">
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
            @click="teamAdd()"
          >
            담당자 변경
          </button>
        </div>
        <div class="rounded-full w-14 h-14 lg:w-16 lg:h-16 bg-white mr-3"></div>
        <div class="flex flex-col justify-center">
          <div class="lg:text-2xl">OOO</div>
          <div class="text-sm lg:text-base">XXX</div>
        </div>
      </div>
    </div>
    <div class="flex flex-col items-center w-full h-full px-10 lg:px-20">
      <div class="flex justify-end w-full">
        <div class="flex h-16 lg:w-24">
          <button
            class="
              my-auto
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
            @click="teamAdd()"
          >
            추가
          </button>
        </div>
        <div class="flex bg-buttonGray rounded-t-lg w-9/12 lg:w-1/4">
          <div
            class="
              flex
              justify-center
              items-center
              w-1/3
              hover:opacity-30
              cursor-pointer
              font-black
            "
          >
            상세내용
          </div>
          <div
            class="
              flex
              justify-center
              items-center
              w-1/3
              hover:opacity-30
              cursor-pointer
              font-black
            "
          >
            URL
          </div>
          <div
            class="
              flex
              justify-center
              items-center
              w-1/3
              hover:opacity-30
              cursor-pointer
              font-black
            "
          >
            히스토리
          </div>
        </div>
      </div>
      <div class="flex justify-center items-center bg-itemGray w-full h-full mb-5">
        <Todo-Detail-Contents />
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import HeaderTodoMenu from '@/components/HeaderTodoMenu.vue';
import TodoStatus from '@/components/TodoStatus.vue';
import TodoDetailContents from '@/components/TodoDetailContents.vue';

export default {
  name: 'TODODETAIL',
  components: {
    Header,
    HeaderTodoMenu,
    TodoStatus,
    TodoDetailContents,
  },
  data() {
    return {
      todoInfo: {
        status: '접수',
        userInfo: {
          name: '최광진',
          teamName: 'F106',
          // 즐겨찾기 여부
          isBookmark: true,
        },
        todoName: '회원가입',
        regDate: '2021-10-14',
        modifyDate: '2021-10-15',
        detailList: [
          {
            name: '조용일',
            content: '회원가입 디자인 견본입니다. https://www.naver.com/',
            regDate: '2021-10-21',
          },
          {
            name: '최광진',
            content: '코멘트입니다. [코멘트](https://www.naver.com/)',
            regDate: '2021-10-18',
          },
        ],
      },
    };
  },
};
</script>
