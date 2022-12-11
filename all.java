    private void loadBankAccountsFromHttp() {
        Callable<String> asyncOperation = new HttpManager(BANK_ACCOUNT_URL);
        Callback<String> mainThreadOperation = mainThreadOperationHttpJson();
        asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
    }

    private Callback<String> mainThreadOperationHttpJson() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                bankAccounts.clear();
                bankAccounts.addAll(BankAccountJsonParser.fromJson(result));
                notifyAdapter();
            }
        };
    }
//pentru json
 public static List<BankAccount> fromJson(String json) {
        try {
            JSONArray array = new JSONArray(json);
            List<BankAccount> result = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                BankAccount account = getBankAccount(array.getJSONObject(i));
                result.add(account);
            }
            return result;
        } catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    private static BankAccount getBankAccount(JSONObject object) throws JSONException {
        String bankName = object.getString(BANK_NAME);
        String cardHolderName = object.getString(CARD_HOLDER_NAME);
        int expirationMonth = object.getInt(EXPIRATION_MONTH);
        int expirationYear = object.getInt(EXPIRATION_YEAR);
        long cardNumber = object.getLong(CARD_NUMBER);

        return new BankAccount(cardHolderName, cardNumber, expirationMonth, expirationYear, bankName);
    }

public class ChartView extends View {

    private final Context context;
    private final Map<String, Integer> source;
    private final Paint paint;

    public ChartView(Context context, Map<String, Integer> source) {
        super(context);
        this.context = context;
        this.source = source;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (source == null || source.isEmpty()) {
            return;
        }

        //valoarea unei bare
        float widthBar = (float) getWidth() / source.size();
        //valoarea maxima
        int maxValue = calculateMax();
        //trasare grafic
        int currentBarPosition = 0;
        for (String label : source.keySet()) {
            //valoare curenta
            int value = source.get(label);
            paint.setColor(generateColor(currentBarPosition));
            //trasare bara
            float x1 = currentBarPosition * widthBar;
            float y1 = (1 - (float) value / maxValue) * getHeight();
            float x2 = x1 + widthBar;
            float y2 = getHeight();
            canvas.drawRect(x1, y1, x2, y2, paint);
            //trasare legenda
            paint.setColor(Color.BLACK);
            paint.setTextSize((float) (0.25 * widthBar));
            float x = (float) ((currentBarPosition + 0.5) * widthBar);
            float y = (float) (0.95 * getHeight());
            canvas.rotate(270, x, y);
            canvas.drawText(context.getString(R.string.chart_legend_template, label, value), x, y, paint);
            canvas.rotate(-270, x, y);
            //trecem la urmatoarea bara
            currentBarPosition++;
        }

    }

    private int generateColor(int currentBarPosition) {
        return currentBarPosition % 2 == 0 ? Color.LTGRAY : Color.CYAN;
    }

    private int calculateMax() {
        int max = 0;
        for (Integer value : source.values()) {
            if (max < value) {
                max = value;
            }
        }
        return max;
    }
}
