<template>
  <div>
    <Header />
    <div v-for="(alarm, index) in alarmList" :key="index">
        <input type="checkbox" v-model="checkList" :value="index">{{alarm.content}}
      <button @click="goTodo(alarm.todoId)">이동</button>
    </div>
    <button @click="check">선택한 알림 삭제</button>
    <button @click="checkAll">모든 알림 삭제</button>
  </div>
</template>

<script>
import { removeAllAlarm, removeAlarm } from "@/api/alarm.js";
import { mapGetters } from "vuex";
import Header from '@/components/Header.vue';

export default {
  name: "Alarm",
  components:{
    Header
  },
  data() {
    return {
      alarmList: [],
      checkList: [],
    };
  },
  created() {
    this.alarmList = this.$route.params.alarmList;
  },
  computed: {
    ...mapGetters(["id"]),
  },
  methods: {
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
