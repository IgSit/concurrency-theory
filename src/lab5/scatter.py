import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.patches as patches


def gen_mean(df: pd.DataFrame):
    new = pd.Series(df.iloc[0])
    new['TotalChange'] = df['TotalChange'].mean()
    return new


if __name__ == '__main__':
    with open('data.csv') as file:
        df = pd.read_csv(file)
        df = df.groupby(['Monitor', 'ThreadTypeAmount']).apply(gen_mean)

    fig, ax = plt.subplots(figsize=(8, 6))
    ax.scatter(x=df.ThreadTypeAmount,
               y=df.TotalChange,
               c=pd.Categorical(df.Monitor).codes,
               cmap='tab20b',
               label=df.Monitor)
    plt.title("Total change of monitors per amount of threads")
    plt.xlabel("Amount of threads per type")
    plt.ylabel("Total change")
    red_patch = patches.Patch(color='darkblue', label='FourCondMonitor')
    blue_patch = patches.Patch(color='pink', label='ThreeLockMonitor')

    plt.legend(handles=[red_patch, blue_patch])
    plt.show()
