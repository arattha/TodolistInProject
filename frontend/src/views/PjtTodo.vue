<template>
  <div class="flex flex-col h-full">
    <Header />
    <Header-Todo-Menu :type="'total'" />
    <div class="flex overflow-auto flex-col h-full">
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

      <div id="scroll_div" class="flex overflow-x-auto px-8 mb-1 scroll_type1 h-full">
        <div class="flex pb-3 mr-8" v-for="(teamInfo, index) in teamInfoList" :key="index">
          <Total-Kanban :teamInfo="teamInfo" :TodoStomp="stompClient"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import HeaderTodoMenu from '@/components/HeaderTodoMenu.vue';
import TotalKanban from '@/components/TotalKanban.vue';
import { mapGetters, mapActions } from 'vuex';
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { getTeam } from "@/api/team.js";

export default {
  name: 'PJTTODO',
  components: {
    Header,
    TotalKanban,
    HeaderTodoMenu,
  },
  data() {
    return {
      teamInfoList:[],
      todoList:[],
      teamList:[],
    };
  },

  created() {
    this.set_project_id('1231231231231');
    this.connect();
    this.set_project_name('프로젝트 명');
  },
  computed: {
    ...mapGetters(['projectId']),
  },
  methods: {
    ...mapActions(['set_project_name', 'set_project_id']),
    connect(){
      
      const serverURL = "http://localhost:8082/todo";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {},
        () => {
          // 소켓 연결 성공
          this.connected = true;

          this.stompClient.debug = () => {};
          
          this.stompClient.send(
            "/server/getTodo",
            JSON.stringify({
              projectId: this.projectId,
            }),
            {}
          );

          // subscribe 로 alarm List 가져오기
          this.stompClient.subscribe("/client/todo/" + this.projectId, (res) => {
            this.todoList = JSON.parse(res.body);
            this.getTeamList();
          });
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
        }
      );
    },
    getTeamList(){
      getTeam(this.projectId, 
      (res) => {
        // team 가져옴

        var tmp = res.object;

        this.teamInfoList = [];

        tmp.forEach((value) =>{
          this.teamInfoList.push({
            teamId: value.id,
            teamName: value.name,
            totalCnt: 330,
            addCnt: 30,
            doneCnt: 30,
            progressCnt: 20,
            todoInfoList: [],
            })
        })
        this.updateList();
      },
      () => {
        console.log("team 가져오기 실패");
      })
    },
    updateList(){

      for(var i = 0 ; i < this.todoList.length ; i++){
        var teamId = this.todoList[i].teamId;

        for(var j = 0 ; j < this.teamInfoList.length ; j++){

          if(this.teamInfoList[j].teamId == teamId){
            this.teamInfoList[j].todoInfoList.push({
              id: this.todoList[i].id,
              title: this.todoList[i].title,
              status: this.todoList[i].status,
              projectId: this.todoList[i].projectId,
              teamId: teamId,
              memberId: this.todoList[i].memberId,
              memberName: this.todoList[i].memberName,
              modifyDate: this.todoList[i].modifyDate,
              regDate: this.todoList[i].regDate
            })

            break;
          }
        }
        
      }
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
      console.log('팀추가');
    },
    todoFilter() {
      console.log('할일 필터');
    },
  },
};
</script>
