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
      <!-- <button
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
        @click="teamAdd()"
      >
        할일추가
      </button> -->
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
        v-for="(statusInfo, index) in statusInfoList"
        :key="index"
        :status="statusInfo.status"
        :todoList="statusInfo.todoList"
        @changeStatus="changeStatus"
      />
    </div>
  </div>
</template>

<script>
import StatusKanban from '@/components/kanban/StatusKanban.vue';
export default {
  name: 'TEAMTODO',
  components: {
    StatusKanban,
  },
  data() {
    return {
      teamList: [],
      selectTeam: '',
      statusInfoList: {
        newStatus: {
          status: 'New',
          todoList: [],
        },
        accepStatus: {
          status: '접수',
          todoList: [],
        },
        progressStatus: {
          status: '진행',
          todoList: [],
        },
        doneStatus: {
          status: '완료',
          todoList: [],
        },
        notProgressStatus: {
          status: '진행하지않음',
          todoList: [],
        },
      },
      todoInfoList: [],
    };
  },
  created() {
    this.setStatusInfoList();
    // 뷰 인스턴스 생성 시 프로젝트에 있는 팀을
    // teamList에 담아준다.
    this.teamList = [
      {
        teamId: '1231231231231',
        teamName: 'Frontend',
      },
      {
        teamId: '2342342342342',
        teamName: 'Backend',
      },
      {
        teamId: '4534534534534',
        teamName: 'QA',
      },
      {
        teamId: '8915234789531',
        teamName: '인사',
      },
    ];

    const findTeam = this.teamList.find((val) => {
      if (val.teamId === this.$route.params.teamId) {
        return true;
      }
    });

    this.selectTeam = findTeam;
    this.changeTeam();
  },
  methods: {
    setStatusInfoList() {
      this.statusInfoList = {
        newStatus: {
          status: 'New',
          todoList: [],
        },
        accepStatus: {
          status: '접수',
          todoList: [],
        },
        progressStatus: {
          status: '진행',
          todoList: [],
        },
        doneStatus: {
          status: '완료',
          todoList: [],
        },
        notProgressStatus: {
          status: '진행하지않음',
          todoList: [],
        },
      };
    },
    changeTeam() {
      // stautsInfoList를 초기화
      this.setStatusInfoList();

      console.log(this.selectTeam);
      // selectTeam에 팀명이 담기게 되므로 이를 이용해서 백엔드에서
      // 해당 팀의 팀원 리스트를 아래 todoInfoList 담으면 된다.
      this.todoInfoList = [
        {
          id: 1111,
          title: 1,
          status: 'New',
          projectId: 1,
          teamId: 1,
          memberId: 1,
          memberName: 'test',
          modifyDate: '2021-11-10',
          regDate: '2021-11-09',
          isBookmark: false,
        },
        {
          id: 2222,
          title: 2,
          status: '진행',
          projectId: 2,
          teamId: 2,
          memberId: 2,
          memberName: 'test',
          modifyDate: '2021-11-10',
          regDate: '2021-11-09',
          isBookmark: false,
        },
        {
          id: 5555,
          title: 5,
          status: '접수',
          projectId: 5,
          teamId: 5,
          memberId: 5,
          memberName: 'test',
          modifyDate: '2021-11-10',
          regDate: '2021-11-09',
          isBookmark: false,
        },
        {
          id: 3333,
          title: 3,
          status: '완료',
          projectId: 3,
          teamId: 3,
          memberId: 3,
          memberName: 'test',
          modifyDate: '2021-11-10',
          regDate: '2021-11-09',
          isBookmark: false,
        },
        {
          id: 4444,
          title: 4,
          status: '진행하지않음',
          projectId: 4,
          teamId: 4,
          memberId: 4,
          memberName: 'test',
          modifyDate: '2021-11-10',
          regDate: '2021-11-09',
          isBookmark: false,
        },
      ];

      // 해당 메소드는 각 상태 별로 할일을 넣게 해준다.
      this.setStatusTodo();
    },
    setStatusTodo() {
      for (let i = 0; i < this.todoInfoList.length; ++i) {
        if (this.todoInfoList[i].status === 'New') {
          this.statusInfoList.newStatus.todoList.push(this.todoInfoList[i]);
        } else if (this.todoInfoList[i].status === '접수') {
          this.statusInfoList.accepStatus.todoList.push(this.todoInfoList[i]);
        } else if (this.todoInfoList[i].status === '진행') {
          this.statusInfoList.progressStatus.todoList.push(this.todoInfoList[i]);
        } else if (this.todoInfoList[i].status === '완료') {
          this.statusInfoList.doneStatus.todoList.push(this.todoInfoList[i]);
        } else {
          this.statusInfoList.notProgressStatus.todoList.push(this.todoInfoList[i]);
        }
      }
    },
    changeStatus(val) {
      if (val.status === 'New') {
        this.statusInfoList.newStatus.todoList[val.index].status = val.status;
      } else if (val.status === '접수') {
        this.statusInfoList.accepStatus.todoList[val.index].status = val.status;
      } else if (val.status === '진행') {
        this.statusInfoList.progressStatus.todoList[val.index].status = val.status;
      } else if (val.status === '완료') {
        this.statusInfoList.doneStatus.todoList[val.index].status = val.status;
      } else {
        this.statusInfoList.notProgressStatus.todoList[val.index].status = val.status;
      }
    },
  },
};
</script>
