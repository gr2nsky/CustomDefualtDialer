package com.example.myapplication.Adapter;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Yoon
 * @created 2021-09-08
 */
public class PhoneBookListItemTouchHelper extends ItemTouchHelper.Callback{

    String TAG = "PhoneBookListItemTouchHelper";
    int isRunning = 0;

    private PhoneBookListItemItemHelperListener listener;

    //생성자로 listview adapter가 구현한 interface를 생성자로 받아 swipte한 대상을 전달한다
    public PhoneBookListItemTouchHelper(PhoneBookListItemItemHelperListener listener) {
        this.listener = listener;
    }

    //드래그 및 스와이프 방향을 제어. 드래그는 사용하지 않고, 양방향 스와이프를 사용한다.
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    //Swipe기능을 사용할 것이므로, 사용가능하도록 ture 반환
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //Swipe시에 interface 호출하여 방향과 해당 리스트뷰 아이템의 포지션을 넘겨준다
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition(), direction, viewHolder);
    }

    //??
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.d(TAG, "Orign position : " + dX + "," + dY);
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            View view = getView(viewHolder);
            getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);
            Log.d(TAG, "moved position : " + dX + "," + dY);
        }
    }

    //View 밖으로 escape되지 않도록 설정 (크기)
//    @Override
//    public float getSwipeEscapeVelocity(float defaultValue) {
//        return defaultValue * 10;
//    }
//    //View 밖으로 escape되지 않도록 설정 (속도 : defualt = 0.5f)
//    @Override
//    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
//        return 2f;
//    }

    //상호작용 종료 및 애니메이션 종료 후 호출
    @Override
    public void clearView(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(getView(viewHolder));
    }

    //Swipe한 viewHolder가 변경될때 호출
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
            getDefaultUIUtil().onSelected(getView(viewHolder));
        }
    }

    //ViewHoleder의 아이템 중, 스와이프된 item의 viewholder를 업캐스팅 하여 view 객체 반환
    private View getView(RecyclerView.ViewHolder viewHolder) {
        View swipeView = ((PhoneBookListAdapter.ViewHolder) viewHolder).swipe_item_phone_book_list;
        return swipeView;
    }



    //-------------------------------------------------------------------------------------------------
    //LongClick에 대한 대응은 허락치 않음. 이후 복수삭제 기능 추가 가능성이 있기에 출동을 방지
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
}
