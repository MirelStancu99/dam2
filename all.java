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
