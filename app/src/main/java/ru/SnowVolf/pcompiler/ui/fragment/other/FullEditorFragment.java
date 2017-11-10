package ru.SnowVolf.pcompiler.ui.fragment.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ru.SnowVolf.girl.ui.CodeEditText;
import ru.SnowVolf.girl.ui.GirlButton;
import ru.SnowVolf.pcompiler.App;
import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.patch.ReactiveBuilder;
import ru.SnowVolf.pcompiler.tabs.TabFragment;

/**
 * Created by Snow Volf on 03.11.2017, 14:42
 */

public class FullEditorFragment extends TabFragment {
    public static final String KEY_CONTENT = "CONTENT";
    private String mContent;
    private RelativeLayout mBackgroundLayout;
    private CodeEditText mEditText;
    private GirlButton mTab, mEsc, mDot, mPlus, mOpenBrace, mCloseBrace, mDollar, mOpenFBrace,
            mCloseFBrace, mOpenBracket, mCloseBracket, mAll, mOr, mGroup;
    private ImageButton mMenuButton;
    
    public static FullEditorFragment newInstance(StringBuilder patchContent){
        Bundle args = new Bundle();
        args.putString(KEY_CONTENT, patchContent.toString());
        
        FullEditorFragment fullEditorFragment = new FullEditorFragment();
        fullEditorFragment.setArguments(args);
        return fullEditorFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            mContent = args.getString(KEY_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setTabTitle(getString(R.string.menu_edit));
        setTitle(getString(R.string.menu_edit));
        baseInflateFragment(inflater, R.layout.fragment_full_editor);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        initLogic();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBackgroundLayout = view.findViewById(R.id.editor_background);
        mEditText = mBackgroundLayout.findViewById(R.id.full_editor);
        mMenuButton = mBackgroundLayout.findViewById(R.id.button_menu);
        LinearLayout mButtonContainer = mBackgroundLayout.findViewById(R.id.buttons_container);
        mTab = mButtonContainer.findViewById(R.id.ed_tab);
        mEsc = mButtonContainer.findViewById(R.id.ed_esc);
        mDot = mButtonContainer.findViewById(R.id.ed_dot);
        mPlus = mButtonContainer.findViewById(R.id.ed_plus);
        mOpenBrace = mButtonContainer.findViewById(R.id.ed_obrace);
        mCloseBrace = mButtonContainer.findViewById(R.id.ed_cobrace);
        mDollar = mButtonContainer.findViewById(R.id.ed_dollar);
        mOpenFBrace = mButtonContainer.findViewById(R.id.ed_ofbrace);
        mCloseFBrace = mButtonContainer.findViewById(R.id.ed_cfbrace);
        mOpenBracket = mButtonContainer.findViewById(R.id.ed_obracket);
        mCloseBracket = mButtonContainer.findViewById(R.id.ed_cbracket);
        mAll = mButtonContainer.findViewById(R.id.ed_all);
        mOr = mButtonContainer.findViewById(R.id.ed_or);
        mGroup = mButtonContainer.findViewById(R.id.ed_group);
        
        mEditText.setUpdateDelay(Integer.parseInt(App.ctx().getPreferences().getString("sys.delay", "2000")));
        mEditText.setText(mContent);
    }

    private void initLogic() {
        mMenuButton.setOnClickListener(view -> {
            PopupMenu pop = new PopupMenu(getContext(), view);
            pop.inflate(R.menu.menu_full_editor);
            pop.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.action_refresh:{
                        mEditText.setText(ReactiveBuilder.build());
                        return true;
                    }
                    case R.id.action_redo:
                        return false;
                    case R.id.action_undo:
                        return false;
                }
                return false;
            });
//            pop.setOnDismissListener(new PopupMenu.OnDismissListener() {
//                @Override
//                public void onDismiss(PopupMenu menu) {
//                    menu.show();
//                }
//            });
            MenuPopupHelper helper = new MenuPopupHelper(getContext(), (MenuBuilder) pop.getMenu(), view);
            helper.setForceShowIcon(true);
            helper.show();
        });

        initButtonsLogic();

    }

    private void initButtonsLogic() {
        final GirlButton[] buttons = {
                mTab, mEsc, mDot, mPlus, mOpenBrace, mCloseBrace, mDollar, mOpenFBrace,
                mCloseFBrace, mOpenBracket, mCloseBracket, mAll, mOr, mGroup
        };
        for (GirlButton button : buttons) {
            button.setOnClickListener(view -> insertText(button.getRealText()));
        }
    }

    public void setText(String text) {
        mEditText.setText(text);
    }

    public boolean insertText(String text) {
        return insertText(text, null);
    }

    public int[] getSelectionRange() {
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        if (selectionEnd < selectionStart && selectionEnd != -1) {
            int c = selectionStart;
            selectionStart = selectionEnd;
            selectionEnd = c;
        }
        return new int[]{selectionStart, selectionEnd};
    }

    public boolean insertText(String startText, String endText) {
        return insertText(startText, endText, true);
    }

    public boolean insertText(String startText, String endText, boolean selectionInside) {
        mEditText.setTranslationY(0);
        int[] selectionRange = getSelectionRange();
        int selectionStart = selectionRange[0];
        int selectionEnd = selectionRange[1];
        if (endText != null && selectionStart != -1 && selectionStart != selectionEnd) {
            mEditText.getText().insert(selectionStart, startText);
            mEditText.getText().insert(selectionEnd + startText.length()/* - 1*/, endText);
            return true;
        }
        mEditText.getText().insert(selectionStart, startText);
        if (endText != null) {
            mEditText.getText().insert(selectionStart + startText.length(), endText);
            if (selectionInside) {
                mEditText.setSelection(selectionStart + startText.length());
            }
        }

        return false;
    }

    public String getSelectedText() {
        int[] selectionRange = getSelectionRange();
        return mEditText.getText().toString().substring(selectionRange[0], selectionRange[1]);
    }

    public void deleteSelected() {
        int[] selectionRange = getSelectionRange();
        mEditText.getText().delete(selectionRange[0], selectionRange[1]);
    }

    public String getMessage() {
        return mEditText.getText().toString();
    }

    public void clearMessage() {
        mEditText.setText("");
    }

}
