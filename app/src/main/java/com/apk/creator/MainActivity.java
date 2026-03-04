package com.apk.creator;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.content.*;
import android.text.*;
import android.text.method.*;
import java.util.*;

public class MainActivity extends Activity {

    EditText etAppName, etPackage, etVersion, etAuthor;
    EditText etMinSdk, etTargetSdk;
    EditText etKeyAlias, etKeyPassword, etStorePassword, etValidity, etOrg;
    TextView tvLog, tvStatus, tvPageTitle;
    ProgressBar progressBar;
    ScrollView logScroll;
    int logCount = 0;

    FrameLayout pageContainer;
    LinearLayout drawerPanel;
    View drawerOverlay;
    boolean drawerAberto = false;

    static final String COR_BG      = "#0D0D1A";
    static final String COR_CARD    = "#1A1A2E";
    static final String COR_DRAWER  = "#12122A";
    static final String COR_PRIMARY = "#7C4DFF";
    static final String COR_ACCENT  = "#00E5FF";
    static final String COR_SUCCESS = "#00E676";
    static final String COR_ERROR   = "#FF1744";
    static final String COR_WARN    = "#FFD740";
    static final String COR_GOLD    = "#FFD700";
    static final String COR_SUBTEXT = "#B0B0CC";

    final Map<String, CheckBox> permChecks = new LinkedHashMap<String, CheckBox>();
    Button btnBuild;
    CheckBox cbDebugKey;

    final String[] PAGINAS = {
        "Info do App",
        "Config SDK",
        "Permissoes",
        "Keystore / Key",
        "Build & APK"
    };
    final String[] ICONES = {"APP","SDK","PRM","KEY","BLD"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        FrameLayout rootFrame = new FrameLayout(this);
        rootFrame.setBackgroundColor(Color.parseColor(COR_BG));

        // ── LAYOUT PRINCIPAL ──
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT));

        // ── TOPBAR ──
        LinearLayout topBar = new LinearLayout(this);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        topBar.setBackground(gradiente(COR_PRIMARY, "#16213E"));
        topBar.setPadding(px(12), px(14), px(16), px(14));

        Button btnMenu = new Button(this);
        btnMenu.setText("|||");
        btnMenu.setTextSize(16);
        btnMenu.setTypeface(null, Typeface.BOLD);
        btnMenu.setTextColor(Color.WHITE);
        btnMenu.setBackground(null);
        btnMenu.setPadding(px(4), px(4), px(12), px(4));
        topBar.addView(btnMenu);

        tvPageTitle = new TextView(this);
        tvPageTitle.setText("Info do App");
        tvPageTitle.setTextSize(18);
        tvPageTitle.setTypeface(null, Typeface.BOLD);
        tvPageTitle.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams titleP =
            new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        tvPageTitle.setLayoutParams(titleP);
        topBar.addView(tvPageTitle);

        TextView tvPro = new TextView(this);
        tvPro.setText("PRO");
        tvPro.setTextSize(10);
        tvPro.setTypeface(null, Typeface.BOLD);
        tvPro.setTextColor(Color.parseColor(COR_DRAWER));
        tvPro.setBackground(fundoSolido(COR_ACCENT, 20));
        tvPro.setPadding(px(8), px(3), px(8), px(3));
        topBar.addView(tvPro);

        mainLayout.addView(topBar);

        pageContainer = new FrameLayout(this);
        pageContainer.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
        mainLayout.addView(pageContainer);

        rootFrame.addView(mainLayout);

        // ── OVERLAY ──
        drawerOverlay = new View(this);
        drawerOverlay.setBackgroundColor(Color.parseColor("#AA000000"));
        drawerOverlay.setLayoutParams(new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT));
        drawerOverlay.setVisibility(View.GONE);
        rootFrame.addView(drawerOverlay);

        // ── DRAWER ──
        drawerPanel = new LinearLayout(this);
        drawerPanel.setOrientation(LinearLayout.VERTICAL);
        drawerPanel.setBackgroundColor(Color.parseColor(COR_DRAWER));
        FrameLayout.LayoutParams drawerLP = new FrameLayout.LayoutParams(
            px(220), FrameLayout.LayoutParams.MATCH_PARENT);
        drawerLP.gravity = Gravity.START;
        drawerPanel.setLayoutParams(drawerLP);
        drawerPanel.setVisibility(View.GONE);
        drawerPanel.setPadding(0, px(16), 0, px(16));

        // Cabeçalho drawer
        LinearLayout drawerHeader = new LinearLayout(this);
        drawerHeader.setOrientation(LinearLayout.VERTICAL);
        drawerHeader.setGravity(Gravity.CENTER);
        drawerHeader.setBackground(gradiente(COR_PRIMARY, "#0D0D1A"));
        drawerHeader.setPadding(px(16), px(24), px(16), px(24));

        TextView drawerLogo = new TextView(this);
        drawerLogo.setText("APK");
        drawerLogo.setTextSize(28);
        drawerLogo.setTypeface(null, Typeface.BOLD);
        drawerLogo.setTextColor(Color.WHITE);
        drawerLogo.setGravity(Gravity.CENTER);
        drawerHeader.addView(drawerLogo);

        TextView drawerSub = new TextView(this);
        drawerSub.setText("Creator Pro");
        drawerSub.setTextSize(12);
        drawerSub.setTextColor(Color.parseColor(COR_ACCENT));
        drawerSub.setGravity(Gravity.CENTER);
        drawerHeader.addView(drawerSub);

        drawerPanel.addView(drawerHeader);

        View sep = new View(this);
        sep.setBackgroundColor(Color.parseColor("#2A2A4A"));
        sep.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, px(1)));
        drawerPanel.addView(sep);
        drawerPanel.addView(gap(8));

        // Itens do drawer
        for (int i = 0; i < PAGINAS.length; i++) {
            final int idx = i;
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.HORIZONTAL);
            item.setGravity(Gravity.CENTER_VERTICAL);
            item.setPadding(px(16), px(14), px(16), px(14));

            TextView badge = new TextView(this);
            badge.setText(ICONES[i]);
            badge.setTextSize(9);
            badge.setTypeface(null, Typeface.BOLD);
            badge.setTextColor(Color.parseColor(COR_DRAWER));
            badge.setBackground(fundoSolido(i == 3 ? COR_GOLD : COR_PRIMARY, 6));
            badge.setPadding(px(5), px(3), px(5), px(3));
            badge.setGravity(Gravity.CENTER);
            item.addView(badge);

            TextView nome = new TextView(this);
            nome.setText("  " + PAGINAS[i]);
            nome.setTextSize(14);
            nome.setTextColor(Color.parseColor(i == 0 ? COR_ACCENT : COR_SUBTEXT));
            item.addView(nome);

            item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    navegarPara(idx);
                    fecharDrawer();
                }
            });
            drawerPanel.addView(item);
        }

        // Rodapé drawer
        LinearLayout drawerFoot = new LinearLayout(this);
        drawerFoot.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        drawerFoot.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
        TextView tvVersao = new TextView(this);
        tvVersao.setText("v1.0  |  com.apk.creator");
        tvVersao.setTextSize(10);
        tvVersao.setTextColor(Color.parseColor("#444466"));
        tvVersao.setGravity(Gravity.CENTER);
        drawerFoot.addView(tvVersao);
        drawerPanel.addView(drawerFoot);

        rootFrame.addView(drawerPanel);
        setContentView(rootFrame);

        navegarPara(0);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (drawerAberto) fecharDrawer();
                else abrirDrawer();
            }
        });
        drawerOverlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { fecharDrawer(); }
        });
    }

    // ── NAVEGAÇÃO ──
    void navegarPara(int idx) {
        pageContainer.removeAllViews();
        tvPageTitle.setText(PAGINAS[idx]);
        switch (idx) {
            case 0: pageContainer.addView(paginaInfo());       break;
            case 1: pageContainer.addView(paginaSdk());        break;
            case 2: pageContainer.addView(paginaPermissoes()); break;
            case 3: pageContainer.addView(paginaKeystore());   break;
            case 4: pageContainer.addView(paginaBuild());      break;
        }
    }

    void abrirDrawer() {
        drawerPanel.setVisibility(View.VISIBLE);
        drawerOverlay.setVisibility(View.VISIBLE);
        drawerAberto = true;
    }

    void fecharDrawer() {
        drawerPanel.setVisibility(View.GONE);
        drawerOverlay.setVisibility(View.GONE);
        drawerAberto = false;
    }

    // ── PÁGINA 0: INFO ──
    ScrollView paginaInfo() {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Color.parseColor(COR_BG));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(px(16), px(16), px(16), px(24));

        l.addView(labelSection("Nome do App"));
        etAppName = inputField("Ex: Meu App Incrivel");
        l.addView(etAppName); l.addView(gap(12));

        l.addView(labelSection("Package Name"));
        etPackage = inputField("Ex: com.meu.app");
        l.addView(etPackage); l.addView(gap(12));

        l.addView(labelSection("Versao"));
        etVersion = inputField("Ex: 1.0.0");
        l.addView(etVersion); l.addView(gap(12));

        l.addView(labelSection("Autor / Empresa"));
        etAuthor = inputField("Ex: Dev Studio");
        l.addView(etAuthor); l.addView(gap(24));

        Button btn = botaoAcao("Proximo: Config SDK  ->", COR_PRIMARY);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { navegarPara(1); }
        });
        l.addView(btn);
        sv.addView(l);
        return sv;
    }

    // ── PÁGINA 1: SDK ──
    ScrollView paginaSdk() {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Color.parseColor(COR_BG));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(px(16), px(16), px(16), px(24));

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout colMin = new LinearLayout(this);
        colMin.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams pMin =
            new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        pMin.setMarginEnd(px(8));
        colMin.setLayoutParams(pMin);
        colMin.addView(labelSection("Min SDK"));
        etMinSdk = inputField("1");
        etMinSdk.setInputType(InputType.TYPE_CLASS_NUMBER);
        colMin.addView(etMinSdk);
        row.addView(colMin);

        LinearLayout colTgt = new LinearLayout(this);
        colTgt.setOrientation(LinearLayout.VERTICAL);
        colTgt.setLayoutParams(
            new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        colTgt.addView(labelSection("Target SDK"));
        etTargetSdk = inputField("29");
        etTargetSdk.setInputType(InputType.TYPE_CLASS_NUMBER);
        colTgt.addView(etTargetSdk);
        row.addView(colTgt);

        l.addView(row); l.addView(gap(16));
        l.addView(labelSection("Selecao Rapida Target SDK"));
        l.addView(gap(8));

        HorizontalScrollView hs = new HorizontalScrollView(this);
        LinearLayout chips = new LinearLayout(this);
        chips.setOrientation(LinearLayout.HORIZONTAL);
        final String[] sdks = {"21","23","26","28","29","30","33","34"};
        for (int i = 0; i < sdks.length; i++) {
            final String sdk = sdks[i];
            Button chip = new Button(this);
            chip.setText(sdk);
            chip.setTextSize(12);
            chip.setTextColor(Color.parseColor(sdk.equals("29") ? COR_WARN : COR_ACCENT));
            chip.setBackground(bordaChip(sdk.equals("29") ? COR_WARN : COR_ACCENT));
            chip.setPadding(px(14), px(8), px(14), px(8));
            LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cp.setMarginEnd(px(8));
            chip.setLayoutParams(cp);
            chip.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { etTargetSdk.setText(sdk); }
            });
            chips.addView(chip);
        }
        hs.addView(chips);
        l.addView(hs); l.addView(gap(16));

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setBackground(fundoSolido(COR_CARD, 12));
        info.setPadding(px(14), px(14), px(14), px(14));
        addInfo(info, "Min SDK 1",    "Compativel com quase todo Android");
        addInfo(info, "Target SDK 29","Android 10 - recomendado");
        addInfo(info, "Target SDK 33+","Android 13 - exige mais permissoes");
        l.addView(info); l.addView(gap(24));

        Button btn = botaoAcao("Proximo: Permissoes  ->", COR_PRIMARY);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { navegarPara(2); }
        });
        l.addView(btn);
        sv.addView(l);
        return sv;
    }

    // ── PÁGINA 2: PERMISSÕES ──
    ScrollView paginaPermissoes() {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Color.parseColor(COR_BG));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(px(16), px(16), px(16), px(24));

        String[][] perms = {
            {"INTERNET",               "Acessa a internet"},
            {"CAMERA",                 "Usa a camera"},
            {"READ_EXTERNAL_STORAGE",  "Le arquivos"},
            {"WRITE_EXTERNAL_STORAGE", "Salva arquivos"},
            {"VIBRATE",                "Vibrar"},
            {"ACCESS_FINE_LOCATION",   "GPS preciso"},
            {"ACCESS_COARSE_LOCATION", "Localizacao aproximada"},
            {"RECORD_AUDIO",           "Microfone"},
            {"RECEIVE_BOOT_COMPLETED", "Inicia com o celular"}
        };

        for (int i = 0; i < perms.length; i++) {
            final String permName = perms[i][0];
            final String permDesc = perms[i][1];

            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.HORIZONTAL);
            item.setGravity(Gravity.CENTER_VERTICAL);
            item.setBackground(fundoSolido(COR_CARD, 10));
            item.setPadding(px(14), px(12), px(14), px(12));
            LinearLayout.LayoutParams ip =
                new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ip.bottomMargin = px(8);
            item.setLayoutParams(ip);

            LinearLayout txtCol = new LinearLayout(this);
            txtCol.setOrientation(LinearLayout.VERTICAL);
            txtCol.setLayoutParams(
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvName = new TextView(this);
            tvName.setText(permName);
            tvName.setTextSize(13);
            tvName.setTypeface(null, Typeface.BOLD);
            tvName.setTextColor(Color.WHITE);
            txtCol.addView(tvName);

            TextView tvDesc = new TextView(this);
            tvDesc.setText(permDesc);
            tvDesc.setTextSize(11);
            tvDesc.setTextColor(Color.parseColor(COR_SUBTEXT));
            txtCol.addView(tvDesc);

            CheckBox cb = new CheckBox(this);
            cb.setButtonTintList(
                android.content.res.ColorStateList.valueOf(
                    Color.parseColor(COR_PRIMARY)));
            permChecks.put(permName, cb);

            item.addView(txtCol);
            item.addView(cb);
            l.addView(item);
        }

        l.addView(gap(24));
        Button btn = botaoAcao("Proximo: Keystore / Key  ->", COR_GOLD);
        btn.setTextColor(Color.parseColor(COR_DRAWER));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { navegarPara(3); }
        });
        l.addView(btn);
        sv.addView(l);
        return sv;
    }

    // ── PÁGINA 3: KEYSTORE ──
    ScrollView paginaKeystore() {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Color.parseColor(COR_BG));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(px(16), px(16), px(16), px(24));

        // Info box
        LinearLayout infoBox = new LinearLayout(this);
        infoBox.setOrientation(LinearLayout.VERTICAL);
        infoBox.setBackground(fundoBorda("#1A1A0A", COR_GOLD, 12));
        infoBox.setPadding(px(14), px(14), px(14), px(14));

        TextView tvExpTitulo = new TextView(this);
        tvExpTitulo.setText("Sua KeyStore atual");
        tvExpTitulo.setTextSize(14);
        tvExpTitulo.setTypeface(null, Typeface.BOLD);
        tvExpTitulo.setTextColor(Color.parseColor(COR_GOLD));
        infoBox.addView(tvExpTitulo);

        TextView tvExpDesc = new TextView(this);
        tvExpDesc.setText(
            "\nArquivo  : KeyStore.jks" +
            "\nAlias    : app" +
            "\nSenha    : AbraCadraBode" +
            "\nValidade : 25 anos\n\n" +
            "O APK sera assinado automaticamente\npelo GitHub Actions!"
        );
        tvExpDesc.setTextSize(13);
        tvExpDesc.setTextColor(Color.parseColor(COR_SUBTEXT));
        tvExpDesc.setLineSpacing(px(3), 1f);
        infoBox.addView(tvExpDesc);
        l.addView(infoBox);
        l.addView(gap(16));

        // Toggle debug
        cbDebugKey = new CheckBox(this);
        cbDebugKey.setText("  Usar Debug Key (testes rapidos, sem KeyStore)");
        cbDebugKey.setTextColor(Color.parseColor(COR_ACCENT));
        cbDebugKey.setTextSize(13);
        cbDebugKey.setChecked(false);
        cbDebugKey.setButtonTintList(
            android.content.res.ColorStateList.valueOf(
                Color.parseColor(COR_ACCENT)));
        l.addView(cbDebugKey);
        l.addView(gap(16));

        // Card key
        LinearLayout cardKey = new LinearLayout(this);
        cardKey.setOrientation(LinearLayout.VERTICAL);
        cardKey.setBackground(fundoBorda(COR_CARD, COR_GOLD, 12));
        cardKey.setPadding(px(16), px(16), px(16), px(16));

        TextView cardTitle = new TextView(this);
        cardTitle.setText("Configuracao Release (KeyStore.jks)");
        cardTitle.setTextSize(13);
        cardTitle.setTypeface(null, Typeface.BOLD);
        cardTitle.setTextColor(Color.parseColor(COR_GOLD));
        cardKey.addView(cardTitle);
        cardKey.addView(gap(12));

        cardKey.addView(labelSection("Key Alias"));
        etKeyAlias = inputField("app");
        cardKey.addView(etKeyAlias); cardKey.addView(gap(10));

        cardKey.addView(labelSection("Key Password"));
        etKeyPassword = inputField("AbraCadraBode");
        etKeyPassword.setInputType(
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cardKey.addView(etKeyPassword); cardKey.addView(gap(10));

        cardKey.addView(labelSection("Store Password"));
        etStorePassword = inputField("AbraCadraBode");
        etStorePassword.setInputType(
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cardKey.addView(etStorePassword); cardKey.addView(gap(10));

        cardKey.addView(labelSection("Organizacao"));
        etOrg = inputField("MeuApp");
        cardKey.addView(etOrg); cardKey.addView(gap(10));

        cardKey.addView(labelSection("Validade (anos)"));
        etValidity = inputField("25");
        etValidity.setInputType(InputType.TYPE_CLASS_NUMBER);
        cardKey.addView(etValidity);

        l.addView(cardKey);
        l.addView(gap(24));

        Button btn = botaoAcao("Proximo: Build & APK  ->", COR_PRIMARY);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { navegarPara(4); }
        });
        l.addView(btn);
        sv.addView(l);
        return sv;
    }

    // ── PÁGINA 4: BUILD ──
    ScrollView paginaBuild() {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Color.parseColor(COR_BG));
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(px(16), px(16), px(16), px(24));

        // Resumo
        LinearLayout resumo = new LinearLayout(this);
        resumo.setOrientation(LinearLayout.VERTICAL);
        resumo.setBackground(fundoSolido(COR_CARD, 12));
        resumo.setPadding(px(14), px(14), px(14), px(14));

        TextView tvR = new TextView(this);
        tvR.setText("Resumo do Build");
        tvR.setTextSize(14);
        tvR.setTypeface(null, Typeface.BOLD);
        tvR.setTextColor(Color.parseColor(COR_ACCENT));
        resumo.addView(tvR);
        resumo.addView(gap(8));

        addInfo(resumo, "App",     etAppName   != null && !etAppName.getText().toString().isEmpty()   ? etAppName.getText().toString()   : "(nao definido)");
        addInfo(resumo, "Package", etPackage   != null && !etPackage.getText().toString().isEmpty()   ? etPackage.getText().toString()   : "(nao definido)");
        addInfo(resumo, "Versao",  etVersion   != null && !etVersion.getText().toString().isEmpty()   ? etVersion.getText().toString()   : "1.0.0");
        addInfo(resumo, "Min SDK", etMinSdk    != null && !etMinSdk.getText().toString().isEmpty()    ? etMinSdk.getText().toString()    : "1");
        addInfo(resumo, "Tgt SDK", etTargetSdk != null && !etTargetSdk.getText().toString().isEmpty() ? etTargetSdk.getText().toString() : "29");
        addInfo(resumo, "Chave",   (cbDebugKey != null && cbDebugKey.isChecked()) ? "Debug Key" : "KeyStore.jks");

        l.addView(resumo); l.addView(gap(16));

        // Console
        TextView tvConsoleLabel = new TextView(this);
        tvConsoleLabel.setText("CONSOLE DE BUILD");
        tvConsoleLabel.setTextSize(12);
        tvConsoleLabel.setTypeface(null, Typeface.BOLD);
        tvConsoleLabel.setTextColor(Color.parseColor(COR_ACCENT));
        l.addView(tvConsoleLabel); l.addView(gap(6));

        LinearLayout cardLog = new LinearLayout(this);
        cardLog.setOrientation(LinearLayout.VERTICAL);
        cardLog.setBackground(fundoSolido("#050510", 12));
        cardLog.setPadding(px(12), px(12), px(12), px(12));

        tvLog = new TextView(this);
        tvLog.setText("> Pronto para compilar...\n");
        tvLog.setTextColor(Color.parseColor(COR_SUCCESS));
        tvLog.setTextSize(12);
        tvLog.setTypeface(Typeface.MONOSPACE);

        logScroll = new ScrollView(this);
        logScroll.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, px(180)));
        logScroll.addView(tvLog);
        cardLog.addView(logScroll);

        tvStatus = new TextView(this);
        tvStatus.setText("PRONTO");
        tvStatus.setTextColor(Color.parseColor(COR_SUCCESS));
        tvStatus.setTextSize(12);
        tvStatus.setTypeface(null, Typeface.BOLD);
        tvStatus.setPadding(0, px(6), 0, 0);
        cardLog.addView(tvStatus);

        progressBar = new ProgressBar(this, null,
            android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.GONE);
        cardLog.addView(progressBar);

        l.addView(cardLog); l.addView(gap(18));

        btnBuild = new Button(this);
        btnBuild.setText("CRIAR APK AGORA");
        btnBuild.setTextSize(16);
        btnBuild.setTypeface(null, Typeface.BOLD);
        btnBuild.setTextColor(Color.WHITE);
        btnBuild.setBackground(gradiente(COR_PRIMARY, "#00B0FF"));
        btnBuild.setPadding(px(20), px(18), px(20), px(18));
        btnBuild.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        l.addView(btnBuild);

        btnBuild.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nome   = etAppName   != null ? etAppName.getText().toString().trim()   : "";
                String pkg    = etPackage   != null ? etPackage.getText().toString().trim()   : "";
                String versao = etVersion   != null ? etVersion.getText().toString().trim()   : "1.0.0";
                String minSdk = etMinSdk    != null ? etMinSdk.getText().toString().trim()    : "1";
                String tgtSdk = etTargetSdk != null ? etTargetSdk.getText().toString().trim() : "29";
                boolean debug = cbDebugKey  != null && cbDebugKey.isChecked();

                if (nome.isEmpty()) {
                    alerta("Nome nao definido!", "Va em Info do App e preencha."); return;
                }
                if (pkg.isEmpty() || !pkg.contains(".")) {
                    alerta("Package invalido!", "Ex: com.meu.app"); return;
                }

                List<String> selPerms = new ArrayList<String>();
                for (Map.Entry<String, CheckBox> e : permChecks.entrySet()) {
                    if (e.getValue().isChecked()) selPerms.add(e.getKey());
                }
                iniciarBuild(nome, pkg, versao, minSdk, tgtSdk, debug, selPerms);
            }
        });

        sv.addView(l);
        return sv;
    }

    // ── BUILD ──
    void iniciarBuild(final String nome, final String pkg, final String versao,
                      final String minSdk, final String tgtSdk,
                      final boolean debug, final List<String> perms) {

        btnBuild.setEnabled(false);
        btnBuild.setText("Compilando...");
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        tvStatus.setText("COMPILANDO...");
        tvStatus.setTextColor(Color.parseColor(COR_WARN));
        tvLog.setText("");
        logCount = 0;

        final String[][] passos = {
            {"0",  "> Verificando configuracoes..."},
            {"8",  "> App: " + nome + "  |  " + pkg},
            {"14", "> Versao: " + (versao.isEmpty() ? "1.0.0" : versao)},
            {"20", "> SDK: " + minSdk + " -> " + tgtSdk},
            {"26", "> Permissoes: " + (perms.isEmpty() ? "nenhuma" : join(perms))},
            {"30", "> Assinatura: " + (debug ? "Debug Key" : "KeyStore.jks (AbraCadraBode)")},
            {"38", "> Gerando AndroidManifest.xml..."},
            {"46", "> Compilando .java -> .class ..."},
            {"54", "> Convertendo .class -> .dex (D8)..."},
            {"62", "> Processando recursos (AAPT2)..."},
            {"70", "> Linkando bibliotecas..."},
            {"78", "> Assinando APK..."},
            {"86", "> ZipAlign + otimizacao..."},
            {"93", "> Salvando " + pkg + ".apk ..."},
            {"100","> APK gerado com sucesso!"}
        };

        final Handler h = new Handler(Looper.getMainLooper());
        for (int i = 0; i < passos.length; i++) {
            final int prog   = Integer.parseInt(passos[i][0]);
            final String msg = passos[i][1];
            final boolean last = (i == passos.length - 1);
            h.postDelayed(new Runnable() {
                public void run() {
                    log(msg, last ? COR_SUCCESS : COR_ACCENT);
                    progressBar.setProgress(prog);
                    logScroll.fullScroll(View.FOCUS_DOWN);
                    if (last) finalizarBuild(nome, pkg, minSdk, tgtSdk, debug);
                }
            }, (i + 1) * 650L);
        }
    }

    void finalizarBuild(final String nome, final String pkg,
                        final String minSdk, final String tgtSdk, boolean debug) {
        tvStatus.setText("CONCLUIDO");
        tvStatus.setTextColor(Color.parseColor(COR_SUCCESS));
        btnBuild.setEnabled(true);
        btnBuild.setText("CRIAR APK AGORA");
        log("> Faca o push no GitHub para gerar o APK real!", COR_WARN);

        new AlertDialog.Builder(this)
            .setTitle("APK Pronto!")
            .setMessage(
                "App    : " + nome + "\n" +
                "Package: " + pkg  + "\n" +
                "SDK    : " + minSdk + " -> " + tgtSdk + "\n" +
                "Chave  : " + (debug ? "Debug Key" : "KeyStore.jks") + "\n\n" +
                "Faca o push no GitHub e\nbaixe o APK pela aba Actions!"
            )
            .setPositiveButton("OK", null)
            .setCancelable(false)
            .show();
    }

    // ── HELPERS ──
    void log(String msg, String cor) {
        logCount++;
        tvLog.append("[" + logCount + "] " + msg + "\n");
        tvLog.setTextColor(Color.parseColor(cor));
    }

    void alerta(String titulo, String msg) {
        new AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .show();
    }

    void addInfo(LinearLayout parent, String chave, String valor) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, px(3), 0, px(3));

        TextView k = new TextView(this);
        k.setText(chave + ": ");
        k.setTextSize(12);
        k.setTypeface(null, Typeface.BOLD);
        k.setTextColor(Color.parseColor(COR_SUBTEXT));
        k.setMinWidth(px(80));
        row.addView(k);

        TextView vv = new TextView(this);
        vv.setText(valor);
        vv.setTextSize(12);
        vv.setTextColor(Color.WHITE);
        row.addView(vv);

        parent.addView(row);
    }

    String join(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    Button botaoAcao(String txt, String cor) {
        Button b = new Button(this);
        b.setText(txt);
        b.setTextSize(14);
        b.setTypeface(null, Typeface.BOLD);
        b.setTextColor(Color.WHITE);
        b.setBackground(fundoSolido(cor, 12));
        b.setPadding(px(20), px(16), px(20), px(16));
        b.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        return b;
    }

    TextView labelSection(String txt) {
        TextView tv = new TextView(this);
        tv.setText(txt);
        tv.setTextSize(12);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(Color.parseColor(COR_SUBTEXT));
        tv.setPadding(0, 0, 0, px(4));
        return tv;
    }

    EditText inputField(String hint) {
        EditText et = new EditText(this);
        et.setHint(hint);
        et.setHintTextColor(Color.parseColor("#555577"));
        et.setTextColor(Color.WHITE);
        et.setTextSize(14);
        et.setPadding(px(14), px(14), px(14), px(14));
        et.setBackground(fundoSolido("#0D0D2A", 10));
        et.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        return et;
    }

    GradientDrawable fundoSolido(String cor, int radius) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(cor));
        gd.setCornerRadius(px(radius));
        return gd;
    }

    GradientDrawable fundoBorda(String fundo, String borda, int radius) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(fundo));
        gd.setStroke(px(1), Color.parseColor(borda));
        gd.setCornerRadius(px(radius));
        return gd;
    }

    GradientDrawable gradiente(String c1, String c2) {
        GradientDrawable gd = new GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            new int[]{Color.parseColor(c1), Color.parseColor(c2)});
        gd.setCornerRadius(px(14));
        return gd;
    }

    GradientDrawable bordaChip(String cor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.TRANSPARENT);
        gd.setStroke(px(1), Color.parseColor(cor));
        gd.setCornerRadius(px(20));
        return gd;
    }

    View gap(int dp) {
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, px(dp)));
        return v;
    }

    int px(int dp) {
        return (int)(dp * getResources().getDisplayMetrics().density);
    }
}
