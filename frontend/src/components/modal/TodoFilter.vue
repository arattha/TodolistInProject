<template>
  <div
    class="
      flex
      justify-center
      items-center
      fixed
      top-0
      left-0
      right-0
      bottom-0
      w-screen
      h-screen
      z-50
      bg-buttonGray bg-opacity-75
    "
  >
    <div
      class="
        flex flex-col
        h-5/6
        lg:w-5/12
        w-9/12
        mx-auto
        rounded-md
        shadow-lg
        fixed
        translate-x-1/2 translate-y-1/2
        bg-white
        z-50
      "
      v-click-outside="onClickOutside"
    >
      <div class="flex flex-col h-full w-full">
        <div
          class="
            flex
            rounded-t-md
            bg-headerGray
            h-1/6
            w-full
            justify-center
            items-center
            text-white
            font-black
            text-3xl
          "
        >
          필터
        </div>
        <div class="flex flex-col h-full w-full bg-itemGray overflow-y-auto px-16">
          <div class="flex flex-col py-10">
            <div class="text-2xl font-bold">할일 상태</div>
            <div class="flex flex-col" id="status">
              <div class="flex items-center my-5">
                <input
                  class="flex mr-5 5 w-4 h-4"
                  type="checkbox"
                  id="allStatus"
                  v-model="selectAllStatus"
                />
                <label class="flex cursor-pointer" for="allStatus">전체선택</label>
              </div>
              <div class="flex w-full justify-center items-center">
                <div
                  class="flex items-center justify-center mx-1 md:mx-3"
                  v-for="(status, index) in statusList"
                  :key="'s' + index"
                >
                  <input
                    class="hidden"
                    type="checkbox"
                    :id="'s' + index"
                    :value="status"
                    @change="selectStatus"
                    v-model="filters.status"
                  />
                  <label :for="'s' + index" class="flex items-center justify-center cursor-pointer">
                    <div
                      class="
                        flex
                        justify-center
                        items-center
                        rounded-2xl
                        shadow-lg
                        w-16
                        h-8
                        font-black
                        hover:opacity-100
                      "
                      :class="{
                        'text-sm':
                          status === 'New' ||
                          status === '접수' ||
                          status === '진행' ||
                          status === '완료',
                        'text-xs': status === '진행하지않음',
                        'text-center': status === '진행하지않음',
                        'bg-green-500': status === 'New',
                        'bg-add': status === '접수',
                        'bg-progress': status === '진행',
                        'bg-done': status === '완료',
                        'bg-buttonGray': status === '진행하지않음',
                        'opacity-30': filters.status.indexOf(status) < 0,
                      }"
                    >
                      <p v-if="status !== '진행하지않음'">{{ status }}</p>
                      <p v-if="status === '진행하지않음'">
                        진행하지 <br />
                        않음
                      </p>
                    </div>
                  </label>
                </div>
              </div>
              <!-- <div v-for="(status, index) in statusList" :key="'s' + index">
                <input type="checkbox" :id="'s' + index" :value="status" v-model="filters.status" />
                <label :for="'s' + index">{{ status }}</label>
              </div> -->
              <br />
            </div>
          </div>
          <div class="flex text-2xl font-bold" id="teams">팀</div>
          <div class="flex items-center my-5">
            <input class="mr-5 w-4 h-4" type="checkbox" id="allTeams" v-model="selectAllTeams" />
            <label class="cursor-pointer" for="allTeams">전체선택</label>
          </div>
          <div
            class="
              flex flex-col
              bg-contentGray
              h-full
              overflow-y-auto
              scroll_type2
              rounded-lg
              shadow-lg
            "
          >
            <div
              class="flex justify-center items-center hover:bg-itemGray h-14"
              v-for="(team, index) in teamInfoList"
              :key="'t' + index"
            >
              <input
                class="w-1/6"
                type="checkbox"
                :id="'t' + index"
                :value="team.teamName"
                v-model="filters.team"
              />
              <label
                class="flex w-5/6 h-full justify-center items-center cursor-pointer"
                :for="'t' + index"
                >{{ team.teamName }}</label
              >
            </div>
          </div>
        </div>

        <div class="flex justify-center items-center p-5 bg-itemGray">
          <button
            class="
              bg-red-600
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-red-800
              focus:outline-none
              focus:ring-2
              focus:ring-red-500
              focus:ring-offset-2
              focus:ring-offset-red-200
              mr-10
            "
            @click="closeModal"
          >
            닫기
          </button>
          <button
            class="
              bg-green-600
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-green-800
              focus:outline-none
              focus:ring-2
              focus:ring-green-500
              focus:ring-offset-2
              focus:ring-offset-green-200
              mr-10
            "
            @click="cleanFilter"
          >
            초기화
          </button>
          <button
            class="
              bg-headerGray
              text-white text-base
              font-semibold
              w-20
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-menuGray
              focus:outline-none
              focus:ring-2
              focus:ring-blue-500
              focus:ring-offset-2
              focus:ring-offset-blue-200
            "
            @click="applyFilter"
          >
            적용
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import vClickOutside from 'v-click-outside';

export default {
  name: 'TodoFilter',
  props: ['teamInfoList'],
  data() {
    return {
      filters: {
        team: [],
        status: [],
      },
      statusList: ['New', '접수', '진행', '완료', '진행하지않음'],
    };
  },
  directives: {
    clickOutside: vClickOutside.directive,
  },
  created() {},
  methods: {
    onClickOutside() {
      this.closeModal();
    },
    closeModal() {
      this.$emit('closeModal');
    },
    applyFilter() {
      this.$emit('applyFilter', this.filters);
    },
    cleanFilter() {
      this.$emit('cleanFilter');
    },
    selectStatus(e) {
      console.log('??', e.target.value);
    },
  },
  computed: {
    selectAllStatus: {
      get: function () {
        return this.statusList.length == this.filters.status.length ? true : false;
      },
      set: function (value) {
        if (value) {
          this.filters.status = this.statusList;
        } else {
          this.filters.status = [];
        }
      },
    },
    selectAllTeams: {
      get: function () {
        return this.teamInfoList.length == this.filters.team.length ? true : false;
      },
      set: function (value) {
        if (value) {
          this.teamInfoList.forEach((team) => {
            this.filters.team.push(team.teamName);
          });
        } else {
          this.filters.team = [];
        }
      },
    },
  },
};
</script>
