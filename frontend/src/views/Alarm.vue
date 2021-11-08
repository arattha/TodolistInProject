<template>
  <div>
    <Header />
    <div v-for="(alarm, index) in alarmList" :key="index">
        <input type="checkbox" v-model="checkList" :value="index">{{alarm.content}}
      <button @click="goTodo(alarm.todoId)">이동</button>
    </div>
    <button @click="check">선택한 알림 삭제</button>
    <button @click="checkAll">모든 알림 삭제</button>
    <Pagination class="mt-3" :pageCnt="pageCnt" :pageSize="pageSize" @paging="pagingMethod" />
  </div>
</template>

<script>
import { removeAllAlarm, removeAlarm, getAlarmInMypage, getAlarmCnt } from "@/api/alarm.js";
import { mapActions, mapGetters } from "vuex";
import Pagination from '@/components/Pagination';
import Header from '@/components/Header.vue';

export default {
  name: "Alarm",
  components:{
    Header,
    Pagination
  },
  data() {
    return {
      alarmList: [],
      checkList: [],
      pageCnt: 5,
      pageSize: 6,
    };
  },
  created() {
    this.pageCnt = 5;
    this.pageSize = 10;
    this.getTotalAlarmCnt();
    this.getAlarm();
  },
  computed: {
    ...mapGetters(["id"]),
  },
  methods: {
    ...mapActions(['set_totalReviewCnt', 'set_curPage', 'set_offset', 'toggle_isDel']),
    async getAlarm() {
      let userData = {
        id: this.id,
        page: this.curPage - 1,
        size: 6,
        sort: 'regDate,desc',
      };

      await getAlarmInMypage(
        userData,
        (res) => {
          this.alarmList = res.object.content;

          if (!this.alarmList && this.isDel) {
            this.set_curPage(this.curPage - 1);
            this.getAlarm();
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
    getTotalAlarmCnt() {
      getAlarmCnt(
        this.id,
        (res) => {
          this.set_totalAlarmCnt(res.object);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    pagingMethod(page) {
      this.getAlarm(page);
    },
    selectAlarm(e){
        this.checkList = [];
        for(let i=0; i < e.options.length; i++) {
            const option = e.options[i];
            if(option.selected) {
                this.checkList.push(option.value);
            }
        }
    },
    check() {

      this.checkList.sort();
      let removeAlarm = [];

      var idx = 0;
      var newArray = [];
      for(var i = 0 ; i < this.alarmList.length ; i++){
        if(i == this.checkList[idx]){
          removeAlarm.push(this.alarmList[i].id);
          idx++;
          continue;
        }
        newArray.push(this.alarmList[i]);
      }
      this.alarmList = newArray;
      this.checkList = [];

      this.remove(removeAlarm);

    },
    remove(list){
      removeAlarm(
        {
          memberId: this.id,
          checkList: list
        },
        () => {

          this.$route.params.alarmStomp.send(
            "/server/getAlarm",
            JSON.stringify({
              memberId: this.id,
            }),
            {}
          );

        },
        () => {
          return;
        }
      );
    },
    checkAll() {
      removeAllAlarm(
        this.id,
        () => {
          this.alarmList = [];

          this.$route.params.alarmStomp.send(
            "/server/getAlarm",
            JSON.stringify({
              memberId: this.id,
            }),
            {}
          );
        },
        () => {}
      );
    },
    goTodo(todoId) {
      // 해당 Todo의 상세 페이지로 이동
      // this.$route.push("/todoInfo");
      console.log(todoId);
    },
  },
};
</script>

<style></style>
