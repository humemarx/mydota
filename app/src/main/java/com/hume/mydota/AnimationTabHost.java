package com.hume.mydota;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

/**
 * Created by tcp on 2015/1/6.
 */
public class AnimationTabHost extends TabHost {
    private Animation slideLeftIn, slideLeftOut;
    private Animation slideRightIn, slideRightOut;

    /*是否打开动画效果*/
    private boolean isOpenAnimation;

    /*记录当前标签页总数*/
    private int mTabCount;

    public AnimationTabHost(Context context,AttributeSet attrs){
        super(context,attrs);
        slideLeftIn = AnimationUtils.loadAnimation(context,R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(context,R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(context,R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(context,R.anim.slide_right_out);
        isOpenAnimation = false;
    }

    public void setOpenAnimation(boolean isOpenAnimation){
        this.isOpenAnimation = isOpenAnimation;
    }

    /**
     * 设置标签滑动动画
     * 动画顺序：左进——左出——右进——右出
     * @param animationResIDs
     * @return
     */
    public boolean setTabAnimation(int[] animationResIDs){
        if(3==animationResIDs.length){
            slideLeftIn = AnimationUtils.loadAnimation(getContext(),animationResIDs[0]);
            slideLeftOut = AnimationUtils.loadAnimation(getContext(),animationResIDs[1]);
            slideRightIn = AnimationUtils.loadAnimation(getContext(), animationResIDs[2]);
            slideRightOut = AnimationUtils.loadAnimation(getContext(),animationResIDs[3]);
            return true;
        }
        else{
            return false;
        }
    }

    public int getTabCount(){
        return mTabCount;
    }

    @Override
    public void addTab(TabSpec tabSpec){
        mTabCount++;
        super.addTab(tabSpec);
    }

    @Override
    public void setCurrentTab(int index){
        int mCurrentID = getCurrentTab();

        if(null!=getCurrentView()){
            if(isOpenAnimation){
                if((mCurrentID == mTabCount-1)&&index == 0){
                    getCurrentView().startAnimation(slideLeftOut);
                }else if(mCurrentID==0 && index == (mTabCount-1)){
                    getCurrentView().startAnimation(slideRightOut);
                }else if(index>mCurrentID){
                    getCurrentView().startAnimation(slideLeftOut);
                }else if(index<mCurrentID){
                    getCurrentView().startAnimation(slideRightOut);
                }
            }
        }

        super.setCurrentTab(index);
        if(isOpenAnimation){
            if((mCurrentID == mTabCount-1)&&index == 0){
                getCurrentView().startAnimation(slideLeftIn);
            }else if(mCurrentID==0 && index == (mTabCount-1)){
                getCurrentView().startAnimation(slideRightIn);
            }else if(index>mCurrentID){
                getCurrentView().startAnimation(slideLeftIn);
            }else if(index<mCurrentID){
                getCurrentView().startAnimation(slideRightIn);
            }
        }
    }

}


